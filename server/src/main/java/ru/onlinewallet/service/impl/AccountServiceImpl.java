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
import ru.onlinewallet.entity.user.UserSettings;
import ru.onlinewallet.repo.account.AccountBillRepository;
import ru.onlinewallet.repo.account.AccountGoalRepository;
import ru.onlinewallet.repo.account.AccountRepository;
import ru.onlinewallet.service.AccountService;
import ru.onlinewallet.service.UserService;
import ru.onlinewallet.util.NumberUtil;

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
    private final AccountRepository accountRepository;
    private final AccountBillRepository accountBillRepository;
    private final UserService userService;
    private final AccountGoalRepository accountGoalRepository;
    private static final Map<String, Double> CURRENCIES_RATES_CACHE = new HashMap<>();

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
    public AccountBill addTransaction(AccountBill accountBill, boolean isPlus, double value) {
        Double currentBalance = accountBill.getBalance();
        double balance = isPlus ? currentBalance + value : currentBalance - value;
        if (balance < 0) {
            throw new RuntimeException("Данная операция приводит к отрицательному балансу. Действие отменено.");
        }
        Account account =
                accountRepository.findById(accountBill.getAccountId()).orElseThrow(() -> new RuntimeException("Счет " +
                        "не найден."));

        account.setLastTransaction(Instant.now());
        accountBill.setBalance(balance);
        accountRepository.save(account);
        return accountBillRepository.save(accountBill);
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
    public AccountGoal saveGoal(AccountGoal goal)
    {
        return accountGoalRepository.save(goal);
    }
}
