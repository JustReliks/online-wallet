package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
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

    private static final List<String> DAY_CATEGORIES = getDayCategories();

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final UserSettingsRepository userSettingsRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public AccountStatistics getStatistics(Long accountId, Long days) throws IOException {
        Account account = accountRepository.getById(accountId);
        List<Transaction> transactions = transactionHistoryRepository.findAllByAccountId(accountId);
        String currency = userSettingsRepository.findByUserId(account.getUserId()).getCurrency();
        Instant date = Instant.now();
        List<String> categories = new LinkedList<>();
        AccountStatistics statistics = getAccountStatisticsForDay(date, transactions, currency);
        if (days == 1) {
            categories = DAY_CATEGORIES;
        } else {
            LineChart chartIncome = new LineChart();
            LineChart chartExpense = new LineChart();

            List<Double> dataIncome = new LinkedList<>();
            List<Double> dataExpense = new LinkedList<>();

            dataExpense.add(getDaySum(statistics.getExpenseLineChart().getSeriesData()));
            dataIncome.add(getDaySum(statistics.getIncomeLineChart().getSeriesData()));
            Calendar calendar = GregorianCalendar.getInstance();

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);

            categories.add((day < 10 ? "0" + day : day) + "." + (month < 10 ? "0" + month : month));

            chartIncome.setSeriesData(dataIncome);
            chartExpense.setSeriesData(dataExpense);

            statistics.setExpenseLineChart(chartExpense);
            statistics.setIncomeLineChart(chartIncome);

            for (int i = 0; i < days - 1; i++) {
                date = date.minus(1, ChronoUnit.DAYS);
                calendar.setTime(Date.from(date));
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH) + 1;
                categories.add((day < 10 ? "0" + day : day) + "." + (month < 10 ? "0" + month : month));
                AccountStatistics tempStat = getAccountStatisticsForDay(date, transactions, currency);
                mergeAccounts(statistics, tempStat);
            }
        }
        createCircleData(statistics);

        Collections.reverse(categories);
        Collections.reverse(statistics.getIncomeLineChart().getSeriesData());
        Collections.reverse(statistics.getExpenseLineChart().getSeriesData());
        statistics.getIncomeLineChart().setCategories(categories);
        statistics.getExpenseLineChart().setCategories(categories);

        return statistics;
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

        mainAccount.getIncomeLineChart().getSeriesData().add(getDaySum(chartIncome.getSeriesData()));
        mainAccount.getExpenseLineChart().getSeriesData().add(getDaySum(chartExpense.getSeriesData()));

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

        for (Transaction transaction : transactions) {
            Instant time = transaction.getDateTime();
            Calendar calendar1 = GregorianCalendar.getInstance();
            calendar1.setTime(Date.from(time));
            if (calendar1.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    calendar1.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    calendar1.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                int hour = calendar1.get(Calendar.HOUR);
                String category = transaction.getCategory().getType();
                if (category.equals(INCOME)) {
                    double value = accountService.convertCurrencies(transaction.getQuantity(),
                            transaction.getAccountBill().getCurrency().getShortName(), currency);
                    dataIncome[hour] += value;
                    rawDataIncome.merge(transaction.getCategory(), value, Double::sum);
                }
                if (category.equals(EXPENSES)) {
                    double value = accountService.convertCurrencies(transaction.getQuantity() * -1,
                            transaction.getAccountBill().getCurrency().getShortName(), currency);
                    dataExpense[hour] += value;
                    rawDataExpense.merge(transaction.getCategory(), value, Double::sum);
                }
            }
        }

        circleChartExpense.setRawData(rawDataExpense);
        circleChartIncome.setRawData(rawDataIncome);

        chartExpense.setCategories(DAY_CATEGORIES);
        chartExpense.setSeriesData(new LinkedList<>(Arrays.asList(dataExpense)));

        chartIncome.setCategories(DAY_CATEGORIES);
        chartIncome.setSeriesData(new LinkedList<>(Arrays.asList(dataIncome)));

        statistics.setExpenseCircleChart(circleChartExpense);
        statistics.setIncomeCircleChart(circleChartIncome);
        statistics.setExpenseLineChart(chartExpense);
        statistics.setIncomeLineChart(chartIncome);

        return statistics;
    }

    private void createCircleData(AccountStatistics stat) {
        List<Pair<String, Double>> circleData = new ArrayList<>();
        AtomicReference<Double> allValue = new AtomicReference<>((double) 0);
        stat.getExpenseCircleChart().getRawData().values().forEach(value -> allValue.updateAndGet(v -> v + value));
        for (Map.Entry<TransactionCategory, Double> entry : stat.getExpenseCircleChart().getRawData().entrySet()) {
            double percent = NumberUtil.round(entry.getValue() / allValue.get());
            String name = entry.getKey().getCode();

            circleData.add(Pair.of(name, percent));
        }
        stat.getExpenseCircleChart().setData(circleData);

        circleData = new ArrayList<>();
        allValue.set(0d);
        stat.getIncomeCircleChart().getRawData().values().forEach(value -> allValue.updateAndGet(v -> v + value));
        for (Map.Entry<TransactionCategory, Double> entry : stat.getIncomeCircleChart().getRawData().entrySet()) {
            double percent = NumberUtil.round(entry.getValue() / allValue.get()) * 100;
            String name = entry.getKey().getCode();

            circleData.add(Pair.of(name, percent));
        }
        stat.getIncomeCircleChart().setData(circleData);
    }

    private static List<String> getDayCategories() {
        List<String> category = new LinkedList<>();
        for (int i = 0; i < 24; i++) {
            category.add((i < 10 ? "0" + i : i) + ":00");
        }
        return category;
    }
}
