package ru.onlinewallet.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onlinewallet.entity.ConvertedBalance;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.entity.account.AccountBill;
import ru.onlinewallet.entity.account.AccountGoal;
import ru.onlinewallet.entity.account.AccountType;
import ru.onlinewallet.entity.user.UserSettings;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    public static final String ECB_EUROFXREF = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    public static final String EUR = "EUR";
    private static final Map<String, Double> CURRENCIES_RATES_CACHE = new HashMap<>();

    private final AccountRepository accountRepository;
    private final AccountBillRepository accountBillRepository;
    private final AccountGoalRepository accountGoalRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final TypeRepository typeRepository;

    private final UserService userService;
    private final TransactionHistoryService transactionHistoryService;
    private final TransactionHistoryRepository transactionHistoryRepository;

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
                                      Long categoryId) {
        Double currentBalance = accountBill.getBalance();
        double newValue = isPlus ? value : -value;
        double balance = currentBalance + newValue;
        if (balance < 0) {
            throw new RuntimeException("Данная операция приводит к отрицательному балансу. Действие отменено.");
        }
        Account account =
                accountRepository.findById(accountBill.getAccountId()).orElseThrow(() -> new RuntimeException("Счет " +
                        "не найден."));

        Instant now = Instant.now();
        account.setLastTransaction(now);
        accountBill.setBalance(balance);
        Account savedAccount = accountRepository.save(account);
        AccountBill savedAccountBill = accountBillRepository.save(accountBill);
        transactionHistoryService.addTransaction(savedAccountBill, userId, categoryId, newValue, now);
        return savedAccountBill;
    }

    @Override
    public Double convertCurrencies(double value, String from, String to) throws IOException {
        if (CURRENCIES_RATES_CACHE.isEmpty()) {
            initCurrenciesRates();
        }
        if (from.equals(to)) {
            return value;
        }
        double fromCurr = from.equals(EUR) ? 1 : CURRENCIES_RATES_CACHE.get(from);
        double toCurr = CURRENCIES_RATES_CACHE.getOrDefault(to, 1.0);
        return NumberUtil.round((value / fromCurr) * toCurr);
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

    @Cacheable("currencies-rates")
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
    public Account updateAccount(Account account) {
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
                goal = accountGoalRepository.getById(account.getGoal().getId());
            }
            goal.setName(account.getGoal().getName());
            goal.setValue(account.getGoal().getValue());
            goal.setDate(account.getGoal().getDate());
            byId.setGoal(goal);

            accountGoalRepository.save(goal);
        } else {
            accountGoalRepository.deleteById(byId.getGoal().getId());
            byId.setGoal(null);
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
}
