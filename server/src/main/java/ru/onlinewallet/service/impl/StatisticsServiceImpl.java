package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Service;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.entity.account.Transaction;
import ru.onlinewallet.entity.account.TransactionCategory;
import ru.onlinewallet.entity.account.statistics.AccountStatistics;
import ru.onlinewallet.entity.account.statistics.CircleChart;
import ru.onlinewallet.entity.account.statistics.LineChart;
import ru.onlinewallet.repo.account.AccountRepository;
import ru.onlinewallet.repo.account.TransactionHistoryRepository;
import ru.onlinewallet.repo.user.UserSettingsRepository;
import ru.onlinewallet.service.AccountService;
import ru.onlinewallet.service.StatisticsService;
import ru.onlinewallet.util.NumberUtil;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private static final String INCOME = "INCOME";
    private static final String EXPENSES = "EXPENSES";

    private LinkedList<String> DAY_CATEGORIES;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final UserSettingsRepository userSettingsRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;


    @Override
    public AccountStatistics getStatistics(Long accountId, Long days) throws IOException, CloneNotSupportedException {
        DAY_CATEGORIES = getDayCategories();
        Account account = accountRepository.getById(accountId);
        List<Transaction> transactions = transactionHistoryRepository.findAllByAccountId(accountId);
        String currency = userSettingsRepository.findByUserId(account.getUserId()).getCurrency();
        Instant date = Instant.now();
        AccountStatistics statistics = getAccountStatisticsForDay(date, transactions, currency);

        calcMainInfo(account, transactions, currency, date, statistics);

        calculateMoneyChartCurrentDay(statistics, account, currency);
        if (days != 1) {
            calcNDays(days, account, transactions, currency, date, statistics);
            DAY_CATEGORIES = getNDaysCategories(Math.toIntExact(days));
        }
        createCircleData(statistics);
        statistics.getMoneyLineChart().setCategories(DAY_CATEGORIES);
        statistics.getIncomeLineChart().setCategories(DAY_CATEGORIES);
        statistics.getExpenseLineChart().setCategories(DAY_CATEGORIES);

        return statistics;
    }

    private void calcMainInfo(Account account, List<Transaction> transactions, String currency, Instant date, AccountStatistics statistics) throws CloneNotSupportedException, IOException {
        AccountStatistics mainInfo = calcMainStatistics(account, transactions, currency, date,
                statistics);

        statistics.setAllTransactions(transactions.size());
        double allIncome = 0;
        double allExpense = 0;
        for (Transaction transaction : transactions) {
            Double quantity = accountService.convertCurrencies(transaction.getQuantity(),
                    transaction.getAccountBill().getCurrency().getShortName(), currency);
            if (quantity > 0) {
                allIncome += quantity;
            } else {
                allExpense += allExpense;
            }
        }
        LinkedList<Double> incomeSeriesData = (LinkedList<Double>) mainInfo.getIncomeLineChart().getSeriesData();
        LinkedList<Double> expensesSeriesData = (LinkedList<Double>) mainInfo.getExpenseLineChart().getSeriesData();

        Double monthIncome = incomeSeriesData.stream().reduce(0.0, Double::sum);
        Double monthExpenses = expensesSeriesData.stream().reduce(0.0, Double::sum);

        Double dayIncome = incomeSeriesData.getLast();
        Double dayExpenses = expensesSeriesData.getLast();

        Double weekIncome =
                incomeSeriesData.subList(incomeSeriesData.size() - 7, incomeSeriesData.size()).stream().reduce(0.0,
                        Double::sum);
        Double weekExpenses =
                expensesSeriesData.subList(expensesSeriesData.size() - 7, incomeSeriesData.size()).stream().reduce(0.0,
                        Double::sum);
        statistics.setIncomes(Arrays.asList(allIncome, dayIncome, weekIncome, monthIncome));
        statistics.setExpenses(Arrays.asList(allExpense, dayExpenses, weekExpenses, monthExpenses));
    }

    private AccountStatistics calcMainStatistics(Account account, List<Transaction> transactions, String currency,
                                                 Instant date,
                                                 AccountStatistics statistics) throws IOException {
        AccountStatistics clone = SerializationUtils.clone(statistics);
        calcNDays(30L, account, transactions, currency, date, clone);

        return clone;
    }

    private void calcNDays(Long days, Account account, List<Transaction> transactions, String currency, Instant date,
                           AccountStatistics statistics) throws IOException {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(Date.from(date));
        LineChart chartIncome = new LineChart();
        LineChart chartExpense = new LineChart();
        LineChart chartMoney = new LineChart();

        LinkedList<Double> dataMoney = new LinkedList<>();
        List<Double> dataIncome = new LinkedList<>();
        List<Double> dataExpense = new LinkedList<>();
        dataMoney.add(accountService.getConvertedBalance(account, currency).getValue());
        Double daySumExpense = getDaySum(statistics.getExpenseLineChart().getSeriesData());
        dataExpense.add(daySumExpense);
        Double daySumIncome = getDaySum(statistics.getIncomeLineChart().getSeriesData());
        dataIncome.add(daySumIncome);
        //dataMoney.addFirst(calculateStartDayMoney(currency, transactions, date, dataMoney.getFirst()));

        chartIncome.setSeriesData(dataIncome);
        chartExpense.setSeriesData(dataExpense);

        statistics.setExpenseLineChart(chartExpense);
        statistics.setIncomeLineChart(chartIncome);
        Instant dateMoney = Instant.now();
        for (int i = 0; i < days - 1; i++) {
            calendar.setTime(Date.from(date));
            date = date.minus(1, ChronoUnit.DAYS);
            //if (i > 0)
                dataMoney.addFirst(calculateStartDayMoney(currency, transactions, dateMoney, dataMoney.getFirst()));

            AccountStatistics tempStat = getAccountStatisticsForDay(date, transactions, currency);
            mergeAccounts(statistics, tempStat);
            dateMoney = dateMoney.minus(1, ChronoUnit.DAYS);
        }
        chartMoney.setSeriesData(dataMoney);
        statistics.setMoneyLineChart(chartMoney);
        Collections.reverse(statistics.getExpenseLineChart().getSeriesData());
        Collections.reverse(statistics.getIncomeLineChart().getSeriesData());
    }


    private Double calculateStartDayMoney(String currency, List<Transaction> transactions,
                                          Instant day, Double endValue) throws IOException {
        AccountStatistics statistics = getAccountStatisticsForDay(day, transactions, currency);
        return endValue - getDaySum(statistics.getIncomeLineChart().getSeriesData()) +
                getDaySum(statistics.getExpenseLineChart().getSeriesData());
    }

    private void calculateMoneyChartCurrentDay(AccountStatistics statistics, Account account, String currency) throws IOException {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        LineChart chartMoney = new LineChart();
        chartMoney.setCategories(DAY_CATEGORIES);
        Double[] data = new Double[24];
        Arrays.fill(data, accountService.getConvertedBalance(account, currency).getValue());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        for (int i = hour - 1; i >= 0; i--) {
            data[i] = data[i + 1] - statistics.getIncomeLineChart().getSeriesData().get(i + 1) +
                    statistics.getExpenseLineChart().getSeriesData().get(i + 1);
        }
        chartMoney.setSeriesData(new LinkedList<>(List.of(data)));
        statistics.setMoneyLineChart(chartMoney);
    }

    private Double getDaySum(List<Double> list) {
        Double value = 0d;
        for (Double val : list) {
            value += val;
        }
        return value;
    }

    private void mergeAccounts(AccountStatistics mainAccount, AccountStatistics tempAccount) {
        LineChart chartIncome = tempAccount.getIncomeLineChart();
        LineChart chartExpense = tempAccount.getExpenseLineChart();

        ((LinkedList<Double>) mainAccount.getIncomeLineChart().getSeriesData()).offerLast(getDaySum(chartIncome.getSeriesData()));
        ((LinkedList<Double>) mainAccount.getExpenseLineChart().getSeriesData()).offerLast(getDaySum(chartExpense.getSeriesData()));

        CircleChart chartIncomeCircle = tempAccount.getIncomeCircleChart();
        CircleChart chartExpenseCircle = tempAccount.getExpenseCircleChart();
        for (Map.Entry<TransactionCategory, Double> entry : chartExpenseCircle.getRawData().entrySet()) {
            mainAccount.getExpenseCircleChart().getRawData().merge(entry.getKey(), entry.getValue(), Double::sum);
        }
        for (Map.Entry<TransactionCategory, Double> entry : chartIncomeCircle.getRawData().entrySet()) {
            mainAccount.getIncomeCircleChart().getRawData().merge(entry.getKey(), entry.getValue(), Double::sum);
        }
    }

    private AccountStatistics getAccountStatisticsForDay(Instant startDate,
                                                         List<Transaction> transactions,
                                                         String currency) throws IOException {
        AccountStatistics statistics = new AccountStatistics();
        LineChart chartIncome = new LineChart();
        LineChart chartExpense = new LineChart();
        CircleChart circleChartIncome = new CircleChart();
        CircleChart circleChartExpense = new CircleChart();
        Date date = Date.from(startDate);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        Double[] dataIncome = new Double[24];
        Double[] dataExpense = new Double[24];
        Arrays.fill(dataExpense, 0.0);
        Arrays.fill(dataIncome, 0.0);

        HashMap<TransactionCategory, Double> rawDataIncome = new HashMap<>();
        HashMap<TransactionCategory, Double> rawDataExpense = new HashMap<>();

        circleChartExpense.setRawData(rawDataExpense);
        for (Transaction transaction : transactions) {
            Instant time = transaction.getDateTime();
            Calendar calendar1 = GregorianCalendar.getInstance();
            calendar1.setTime(Date.from(time));
            if (sameDay(calendar1, calendar)) {
                int hour = calendar1.get(Calendar.HOUR_OF_DAY);
                String category = transaction.getCategory().getType();
                if (category.equals(INCOME)) {
                    double value = NumberUtil.round(accountService.convertCurrencies(transaction.getQuantity(),
                            transaction.getAccountBill().getCurrency().getShortName(), currency));
                    dataIncome[hour] += value;
                    rawDataIncome.merge(transaction.getCategory(), value, Double::sum);
                }
                if (category.equals(EXPENSES)) {
                    double value = NumberUtil.round(accountService.convertCurrencies(transaction.getQuantity() * -1,
                            transaction.getAccountBill().getCurrency().getShortName(), currency));
                    dataExpense[hour] += value;
                    rawDataExpense.merge(transaction.getCategory(), value, Double::sum);
                }
            }
        }
        circleChartIncome.setRawData(rawDataIncome);

        chartExpense.setCategories(DAY_CATEGORIES);
        LinkedList<Double> seriesData = new LinkedList<>(Arrays.asList(dataExpense));
        //Collections.reverse(seriesData);
        chartExpense.setSeriesData(seriesData);

        chartIncome.setCategories(DAY_CATEGORIES);
        LinkedList<Double> seriesData1 = new LinkedList<>(Arrays.asList(dataIncome));
       //  Collections.reverse(seriesData1);
        chartIncome.setSeriesData(seriesData1);

        statistics.setExpenseCircleChart(circleChartExpense);
        statistics.setIncomeCircleChart(circleChartIncome);
        statistics.setExpenseLineChart(chartExpense);
        statistics.setIncomeLineChart(chartIncome);

        return statistics;
    }

    private boolean sameDay(Instant instant1, Instant instant2) {
        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setTime(Date.from(instant1));

        Calendar calendar2 = GregorianCalendar.getInstance();
        calendar2.setTime(Date.from(instant2));

        return sameDay(calendar1, calendar2);
    }

    private boolean sameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }


    private void createCircleData(AccountStatistics stat) {
        List<List<Object>> circleData = new ArrayList<>();
        AtomicReference<Double> allValue = new AtomicReference<>((double) 0);
        stat.getExpenseCircleChart().getRawData().values().forEach(value -> allValue.updateAndGet(v -> v + value));
        for (Map.Entry<TransactionCategory, Double> entry : stat.getExpenseCircleChart().getRawData().entrySet()) {
            double percent = NumberUtil.round(entry.getValue() / allValue.get());
            String name = entry.getKey().getTitle();

            circleData.add(List.of(name, percent));
        }
        stat.getExpenseCircleChart().setData(circleData);

        circleData = new ArrayList<>();
        allValue.set(0d);
        stat.getIncomeCircleChart().getRawData().values().forEach(value -> allValue.updateAndGet(v -> v + value));
        for (Map.Entry<TransactionCategory, Double> entry : stat.getIncomeCircleChart().getRawData().entrySet()) {
            double percent = NumberUtil.round(entry.getValue() / allValue.get()) * 100;
            String name = entry.getKey().getTitle();

            circleData.add(List.of(name, percent));
        }
        stat.getIncomeCircleChart().setData(circleData);
    }

    private LinkedList<String> getDayCategories() {
        LinkedList<String> category = new LinkedList<>();
        for (int i = 0; i < 24; i++) {
            category.addFirst((i < 10 ? "0" + i : i) + ":00");
        }
        Collections.reverse(category);
        return category;
    }

    private LinkedList<String> getNDaysCategories(int days) {
        LinkedList<String> category = new LinkedList<>();
        Instant date = Instant.now();
        Calendar calendar = GregorianCalendar.getInstance();
        for (int i = 0; i < days; i++) {
            calendar.setTime(Date.from(date));
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            category.addFirst((day < 10 ? "0" + day : day) + "." + (month < 10 ? "0" + month : month));
            date = date.minus(1, ChronoUnit.DAYS);
        }
        return category;
    }

}
//    @Override
//    public AccountStatistics getStatistics(Long accountId, Long days) throws IOException, CloneNotSupportedException {
//        AccountStatistics statistics;
//        Account account = accountRepository.getById(accountId);
//        String currency = userSettingsRepository.findByUserId(account.getUserId()).getCurrency();
//        List<Transaction> transactions = transactionHistoryRepository.findAllByAccountId(accountId);
//
//        if (days == 1) {
//            DAY_CATEGORIES = getDayCategories();
//            statistics = fillStatisticForCurrentDay(account, currency, transactions);
//        } else {
//            DAY_CATEGORIES = getNDaysCategories(days.intValue());
//            statistics = fillStatisticForSomeDays(account, currency, transactions, Math.toIntExact(days));
//        }
//
//        statistics.setAllTransactions(transactions.size());
//        statistics.getIncomeLineChart().setCategories(DAY_CATEGORIES);
//        statistics.getExpenseLineChart().setCategories(DAY_CATEGORIES);
//        statistics.getMoneyLineChart().setCategories(DAY_CATEGORIES);
//
//        return statistics;
//    }
//
//    private AccountStatistics fillStatisticForSomeDays(Account account, String currency, List<Transaction> transactions, int days) throws IOException {
//        AccountStatistics statistics = getEmptyStatistic();
//        Double[] dataIncome = new Double[days];
//        Double[] dataExpenses = new Double[days];
//        Double[] dataMoney = new Double[days];
//
//        Arrays.fill(dataExpenses, 0.0);
//        Arrays.fill(dataIncome, 0.0);
//        Arrays.fill(dataMoney, 0.0);
//
//        HashMap<TransactionCategory, Double> rawDataIncome = new HashMap<>();
//        HashMap<TransactionCategory, Double> rawDataExpense = new HashMap<>();
//
//        Instant date = Instant.now();
//        Instant now = Instant.now();
//        AccountStatistics prevDayStatistic = fillStatisticForCurrentDay(account, currency, transactions);
//        dataIncome[days - 1] = calculateAll(prevDayStatistic.getIncomeLineChart().getSeriesData());
//        dataExpenses[days - 1] = calculateAll(prevDayStatistic.getExpenseLineChart().getSeriesData());
//
//        for (int i = days - 2; i >= 0; i--) {
//            date = date.minus(1, ChronoUnit.DAYS);
//            double finalBalance;
//            if (i == days - 2) {
//                finalBalance = prevDayStatistic.getMoneyLineChart().getSeriesData().get(23 - getCalendar(now).get(Calendar.HOUR_OF_DAY));
//            } else finalBalance = prevDayStatistic.getMoneyLineChart().getSeriesData().get(0);
//
//            prevDayStatistic = fillFullDayStatistic(account, currency, transactions, date, finalBalance);
//            dataIncome[i] = calculateAll(prevDayStatistic.getIncomeLineChart().getSeriesData());
//            dataExpenses[i] = calculateAll(prevDayStatistic.getExpenseLineChart().getSeriesData());
//
//            mergeMaps(rawDataExpense, prevDayStatistic.getExpenseCircleChart().getRawData());
//            mergeMaps(rawDataIncome, prevDayStatistic.getIncomeCircleChart().getRawData());
//        }
//
//        dataMoney[days - 1] = accountService.getConvertedBalance(account, currency).getValue();
//        for (int i = days - 2; i >= 0; i--) {
//            dataMoney[i] = dataMoney[i + 1] - dataIncome[i + 1] + dataExpenses[i + 1];
//        }
//
//        statistics.getMoneyLineChart().setSeriesData(asLinkedList(dataMoney));
//        statistics.getIncomeLineChart().setSeriesData(asLinkedList(dataIncome));
//        statistics.getExpenseLineChart().setSeriesData(asLinkedList(dataExpenses));
//
//        return statistics;
//    }
//
//
//    private Map<TransactionCategory, Double> mergeMaps(Map<TransactionCategory, Double> map1, Map<TransactionCategory, Double> map2) {
//        for (Map.Entry<TransactionCategory, Double> entry : map2.entrySet()) {
//            map1.merge(entry.getKey(), entry.getValue(), Double::sum);
//        }
//        return map1;
//    }
//
//    private Calendar getCalendar(Instant instant)
//    {
//        Calendar calendar = GregorianCalendar.getInstance();
//        calendar.setTime(Date.from(instant));
//        return calendar;
//    }
//
//
//    private AccountStatistics fillFullDayStatistic(Account account, String currency, List<Transaction> transactions, Instant date, Double finalBalance) throws IOException {
//        AccountStatistics statistics = getEmptyStatistic();
//        Double[] dataIncome = new Double[24];
//        Double[] dataExpenses = new Double[24];
//        Double[] dataMoney = new Double[24];
//
//        HashMap<TransactionCategory, Double> rawDataIncome = new HashMap<>();
//        HashMap<TransactionCategory, Double> rawDataExpense = new HashMap<>();
//
//
//        Arrays.fill(dataExpenses, 0.0);
//        Arrays.fill(dataIncome, 0.0);
//        Arrays.fill(dataMoney, 0.0);
//
//        for (int i = 23; i >= 0; i--) {
//            for (Transaction transaction : transactions) {
//                if (sameDay(date, transaction.getDateTime()) &&
//                        i == getCalendar(transaction.getDateTime()).get(Calendar.HOUR_OF_DAY)) {
//                    String category = transaction.getCategory().getType();
//                    double value = accountService.convertCurrencies(transaction.getQuantity(),
//                            transaction.getAccountBill().getCurrency().getShortName(), currency);
//
//                    if (category.equals(INCOME)) {
//                        dataIncome[i] += value;
//                        rawDataIncome.merge(transaction.getCategory(), value, Double::sum);
//                    }
//                    if (category.equals(EXPENSES)) {
//                        dataExpenses[i] -= value;
//                        rawDataExpense.merge(transaction.getCategory(), value, Double::sum);
//                    }
//                }
//            }
//        }
//        dataMoney[23] = finalBalance;
//        for (int i = 22; i >= 0; i--) {
//            dataMoney[i] = dataMoney[i + 1] - dataIncome[i + 1] + dataExpenses[i + 1];
//        }
//
//        statistics.getMoneyLineChart().setSeriesData(asLinkedList(dataMoney));
//        statistics.getExpenseLineChart().setSeriesData(asLinkedList(dataExpenses));
//        statistics.getIncomeLineChart().setSeriesData(asLinkedList(dataIncome));
//        statistics.getExpenseCircleChart().setRawData(rawDataExpense);
//        statistics.getIncomeCircleChart().setRawData(rawDataIncome);
//
//        return statistics;
//    }
//
//    private List<Double> asLinkedList(Double... values) {
//        LinkedList<Double> list = new LinkedList<>();
//        for (Double val : values) {
//            list.addFirst(val);
//        }
//        Collections.reverse(list);
//        return list;
//    }
//
//    private AccountStatistics getEmptyStatistic() {
//        AccountStatistics statistics = new AccountStatistics();
//        LineChart chartIncome = new LineChart();
//        LineChart chartExpenses = new LineChart();
//        LineChart chartMoney = new LineChart();
//
//        CircleChart circleChartIncome = new CircleChart();
//        CircleChart circleChartExpenses = new CircleChart();
//
//        statistics.setMoneyLineChart(chartMoney);
//        statistics.setExpenseLineChart(chartExpenses);
//        statistics.setIncomeLineChart(chartIncome);
//
//        statistics.setIncomeCircleChart(circleChartIncome);
//        statistics.setExpenseCircleChart(circleChartExpenses);
//
//        return statistics;
//    }
//
//    private AccountStatistics fillStatisticForCurrentDay(Account account, String currency, List<Transaction> transactions) throws IOException {
//        AccountStatistics statistics = getEmptyStatistic();
//        Instant date = Instant.now();
//        Calendar calendar = GregorianCalendar.getInstance();
//        calendar.setTime(Date.from(date));
//        Double[] dataIncome = new Double[24];
//        Double[] dataExpenses = new Double[24];
//        Double[] dataMoney = new Double[24];
//
//        HashMap<TransactionCategory, Double> rawDataIncome = new HashMap<>();
//        HashMap<TransactionCategory, Double> rawDataExpense = new HashMap<>();
//
//
//        Arrays.fill(dataExpenses, 0.0);
//        Arrays.fill(dataIncome, 0.0);
//        Arrays.fill(dataMoney, 0.0);
//
//        for (int i = 23; i >= 0; i--) {
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//            for (Transaction transaction : transactions) {
//                Calendar calendar1 = GregorianCalendar.getInstance();
//                calendar1.setTime(Date.from(transaction.getDateTime()));
//                if (sameDay(calendar1, calendar) &&
//                        calendar.get(Calendar.HOUR_OF_DAY) == calendar1.get(Calendar.HOUR_OF_DAY)) {
//                    String category = transaction.getCategory().getType();
//                    double value = accountService.convertCurrencies(transaction.getQuantity(),
//                            transaction.getAccountBill().getCurrency().getShortName(), currency);
//
//                    if (category.equals(INCOME)) {
//                        dataIncome[hour] += value;
//                        rawDataIncome.merge(transaction.getCategory(), value, Double::sum);
//                    }
//                    if (category.equals(EXPENSES)) {
//                        dataExpenses[hour] -= value;
//                        rawDataExpense.merge(transaction.getCategory(), value, Double::sum);
//                    }
//                }
//            }
//            date = date.minus(1, ChronoUnit.HOURS);
//        }
//        dataMoney[23] = accountService.getConvertedBalance(account, currency).getValue();
//        for (int i = 22; i >= 0; i--) {
//            dataMoney[i] = dataMoney[i + 1] - dataIncome[i + 1] + dataExpenses[i + 1];
//        }
//
//        statistics.getMoneyLineChart().setSeriesData(asLinkedList(dataMoney));
//        statistics.getExpenseLineChart().setSeriesData(asLinkedList(dataExpenses));
//        statistics.getIncomeLineChart().setSeriesData(asLinkedList(dataIncome));
//        statistics.getExpenseCircleChart().setRawData(rawDataExpense);
//        statistics.getIncomeCircleChart().setRawData(rawDataIncome);
//        formCircleCharts(statistics);
//
//        return statistics;
//    }
//
//    private void formCircleCharts(AccountStatistics statistics) {
//        HashMap<TransactionCategory, Double> rawDataIncome = statistics.getIncomeCircleChart().getRawData();
//        HashMap<TransactionCategory, Double> rawDataExpense = statistics.getExpenseCircleChart().getRawData();
//
//        double fullValueIncome = 0;
//        for (Double val : rawDataIncome.values()) {
//            fullValueIncome += val;
//        }
//
//        double fullValueExpenses = 0;
//        for (Double val : rawDataExpense.values()) {
//            fullValueExpenses += val;
//        }
//
//        List<List<Object>> dataIncome = new ArrayList<>();
//
//        for (Map.Entry<TransactionCategory, Double> entry : rawDataIncome.entrySet()) {
//            dataIncome.add(List.of(entry.getKey().getCode(), 100 * fullValueIncome / entry.getValue()));
//        }
//
//        List<List<Object>> dataExpense = new ArrayList<>();
//        for (Map.Entry<TransactionCategory, Double> entry : rawDataExpense.entrySet()) {
//            dataExpense.add(List.of(entry.getKey().getCode(), 100 * fullValueExpenses / entry.getValue()));
//        }
//        statistics.getExpenseCircleChart().setData(dataExpense);
//        statistics.getIncomeCircleChart().setData(dataIncome);
//
//    }
//
//
//    private Double calculateAll(List<Double> list) {
//        Double value = 0d;
//
//        for (Double val : list) {
//            value += val;
//        }
//        return value;
//    }
