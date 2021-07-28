package ru.onlinewallet.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onlinewallet.entity.ConvertedBalance;
import ru.onlinewallet.entity.account.*;
import ru.onlinewallet.entity.user.UserSettings;
import ru.onlinewallet.enums.AccountTypeEnum;
import ru.onlinewallet.repo.account.*;
import ru.onlinewallet.service.AccountService;
import ru.onlinewallet.service.TransactionHistoryService;
import ru.onlinewallet.service.UserService;
import ru.onlinewallet.util.NumberUtil;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    public static final String ECB_EUROFXREF = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    public static final String EUR = "EUR";

    private final AccountRepository accountRepository;
    private final AccountBillRepository accountBillRepository;
    private final AccountGoalRepository accountGoalRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final TypeRepository typeRepository;

    private final UserService userService;
    private final TransactionHistoryService transactionHistoryService;
    private final TransactionHistoryRepository transactionHistoryRepository;

    private static Map<String, Double> CURRENCIES_RATES_CACHE;
    private final ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<AccountBill> createAccountBills(Long id, List<AccountBill> collect) {
        if (Objects.isNull(id)) {
            throw new RuntimeException("Id счета не может быть null");
        }

        collect.forEach(bill -> bill.setAccountId(id));

        return accountBillRepository.saveAll(collect);
    }

    @Override
    public List<Account> getAll(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public AccountBill addTransaction(AccountBill accountBill, Long userId, boolean isPlus, double value,
                                      Long categoryId, Instant transactionDate) throws IOException {
        Account account;
        account = accountRepository.getById(accountBill.getAccountId());

        if (!isPlus && account.getAccountType().getType().getCode().equals(AccountTypeEnum.SAVING.getName()) && account.getFreezeDate().isAfter(transactionDate)) {
            throw new RuntimeException("Данный счет заморожен для списаний!");
        }

        Double currentBalance = accountBill.getBalance();
        double newValue = isPlus ? value : -value;
        double balance = currentBalance + newValue;

        if (balance < 0 && !account.getAccountType().getType().getCode().equals(AccountTypeEnum.CREDIT.getName())) {
            throw new RuntimeException("Данная операция приводит к отрицательному балансу. Действие отменено.");
        }

        if (account.getLastTransaction().isBefore(transactionDate)) {
            account.setLastTransaction(transactionDate);
        }
        accountBill.setBalance(balance);

        AccountBill savedAccountBill = accountBillRepository.save(accountBill);
        if (isPlus) {
            account.setMaxBalance(Math.max(account.getMaxBalance(),
                    getConvertedBalance(account, userService.getUserProfile(account.getUserId()).getCurrency()).getValue()));
        }
        Account savedAccount = accountRepository.save(account);

        AccountGoal goal = account.getGoal();
        if (Objects.nonNull(goal) && !goal.isCompleted()) {
            ConvertedBalance convertedBalance = getConvertedBalance(savedAccount,
                    account.getAccountBills().get(0).getCurrency().getShortName());
            if (convertedBalance.getValue() >= goal.getValue()) {
                goal.setCompleted(true);
                accountGoalRepository.save(goal);
            }
        }
        transactionHistoryService.addTransaction(savedAccountBill, userId, categoryId, newValue, transactionDate);

        return savedAccountBill;
    }

    @Override
    public Double convertCurrencies(double value, String from, String to) throws IOException {
        if (Objects.isNull(CURRENCIES_RATES_CACHE) || CURRENCIES_RATES_CACHE.isEmpty()) {
            CURRENCIES_RATES_CACHE = new HashMap<>();
            initCurrenciesRates();
        }
        if (from.equals(to)) {
            return value;
        }
        try {
            double fromCurr = from.equals(EUR) ? 1 : CURRENCIES_RATES_CACHE.get(from);
            double toCurr = CURRENCIES_RATES_CACHE.getOrDefault(to, 1.0);
            return NumberUtil.round((value / fromCurr) * toCurr);
        } catch (Exception e) {
            System.out.println(to + " | " + from);
            return 0.0d;
        }
    }

    @Override
    public ConvertedBalance getConvertedBalance(Account account, String currency) throws IOException {
        Double value = 0.0;
        for (AccountBill bill : account.getAccountBills()) {
            value += convertCurrencies(bill.getBalance(), bill.getCurrency().getShortName(), currency);
        }
        return new ConvertedBalance(currency, value);
    }


    @Override
    public ConvertedBalance getConvertedBalance(Long id, String currency) throws IOException {
        double sum = 0;
        if (Objects.isNull(currency) || currency.length() > 3) {
            UserSettings userProfile = userService.getUserProfile(id);
            currency = userProfile.getCurrency();
        }

        List<Account> allByUserId = accountRepository.findAllByUserId(id);
        for (Account account : allByUserId) {
            for (AccountBill accountBill : account.getAccountBills()) {
                Double converted = convertCurrencies(accountBill.getBalance(), accountBill.getCurrency().getShortName()
                        , currency);
                sum += converted;
            }
        }
        return new ConvertedBalance(currency, sum);
    }

    public void initCurrenciesRates() throws IOException {
        URL url = new URL(ECB_EUROFXREF);
        URLConnection conn = url.openConnection();
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode jsonNode = xmlMapper.readTree(conn.getInputStream());
        String cube = "Cube";
        String currency = "currency";
        String rate = "rate";
        Iterator<JsonNode> elements = jsonNode.get(cube).get(cube).get(cube).elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            CURRENCIES_RATES_CACHE.put(next.get(currency).asText(), next.get(rate).asDouble());
        }
    }

    @Override
    public AccountGoal saveGoal(AccountGoal goal) {
        if (goal.getName() == null || goal.getName().isEmpty()) {
            throw new RuntimeException("Название цели не может быть null!");
        }

        if (goal.getValue() == 0) {
            throw new RuntimeException("Значение цели не может быть 0");
        }
        return accountGoalRepository.save(goal);
    }

    @Override
    public Account updateAccount(Account account) throws IOException {
        if (Objects.isNull(account.getId())) {
            throw new EntityNotFoundException();
        }
        Account byId = accountRepository.getById(account.getId());
        byId.setName(account.getName());
        byId.setDescription(account.getDescription());
        AccountGoal goal;
        if (account.getGoal() != null) {
            if (byId.getGoal() == null) {
                goal = new AccountGoal();
                goal.setAccountId(byId.getId());
                goal.setAccount(byId);
            } else {
                goal = accountGoalRepository.getById(byId.getGoal().getId());
            }
            goal.setName(account.getGoal().getName());
            goal.setValue(account.getGoal().getValue());
            goal.setDate(account.getGoal().getDate());
            goal.setCompleted(getConvertedBalance(byId, byId.getAccountBills().get(0).getCurrency().getShortName()).getValue() >= goal.getValue());
            byId.setGoal(goal);

            accountGoalRepository.save(goal);

        } else {
            AccountGoal g = byId.getGoal();
            if (Objects.nonNull(g)) {
                accountGoalRepository.deleteById(byId.getGoal().getId());
                byId.setGoal(null);
            }
        }
        accountRepository.save(byId);

        return byId;
    }

    @Override
    public List<AccountType> getAllAccountTypes() {
        return accountTypeRepository.findAll();
    }

    @Override
    public AccountType createAccountType(AccountType accountType) {
        return accountTypeRepository.save(accountType);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.getById(id);
        AccountGoal goal = account.getGoal();
        if (goal != null) {
            accountGoalRepository.deleteById(goal.getId());
        }
        transactionHistoryRepository.findAllByUserId(account.getUserId()).stream().filter(transaction
                -> transaction.getAccountId().equals(id)).
                forEach(transactionHistoryRepository::delete);
        accountRepository.deleteById(account.getId());
    }

    @Override
    public CreditInfo calculateCreditInfo(Account acc) {

        List<AccountBill> bills = acc.getAccountBills();
        if (bills.isEmpty()) {
            return null;
        }
        AccountBill accountBill = bills.get(0);
        LocalDate matureDate = LocalDate.ofInstant(accountBill.getMaturityDate(), ZoneId.systemDefault());
        LocalDate now = LocalDate.now();
        Double creditAmount = accountBill.getStartBalance();
        double rate = accountBill.getRate();
        long between = ChronoUnit.MONTHS.between(now, matureDate);

        double v = -accountBill.getBalance();
        double monthlyPayment;
        if (rate <= 0) {
            monthlyPayment = v / between;
        } else {
            double ratePercentage = rate / 100;
            double pow = Math.pow(1 + ratePercentage, between);
            double v1 = ratePercentage / (pow - 1);
            double v2 = ratePercentage + v1;
            monthlyPayment = v * v2;
        }
        CreditInfo creditInfo = new CreditInfo();
        creditInfo.setMonthlyPayment(monthlyPayment);
        creditInfo.setMaturityDate(accountBill.getMaturityDate());
        creditInfo.setCreditAmount(creditAmount);
        creditInfo.setCurrentCreditBalance(accountBill.getBalance());
        return creditInfo;
    }

    @Override
    public Double calculateGoalDailyPayment(Account acc) throws IOException {
        AccountGoal goal = acc.getGoal();
        Double converted =
                getConvertedBalance(acc, acc.getAccountBills().get(0).getCurrency().getShortName()).getValue();
        Double goalValue = goal.getValue();

        if (goalValue <= converted) return 0d;
        LocalDate matureDate = LocalDate.ofInstant(goal.getDate(), ZoneId.systemDefault());
        LocalDate now = LocalDate.now();

        long distance = ChronoUnit.DAYS.between(now, matureDate);
        if (distance == 0) return goalValue - converted;

        return (goalValue - converted) / distance;
    }

    @Override
    public int getCountAllAccounts(Long userId) {
        return accountRepository.countAccountsByUserId(userId);
    }

}
