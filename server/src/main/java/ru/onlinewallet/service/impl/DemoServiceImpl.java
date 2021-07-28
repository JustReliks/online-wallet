package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.onlinewallet.entity.account.Currency;
import ru.onlinewallet.entity.account.*;
import ru.onlinewallet.enums.AccountTypeEnum;
import ru.onlinewallet.repo.account.*;
import ru.onlinewallet.service.AccountService;
import ru.onlinewallet.service.DemoService;
import ru.onlinewallet.service.DictionaryService;
import ru.onlinewallet.service.UserService;
import ru.onlinewallet.util.NumberUtil;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DemoServiceImpl implements DemoService {

    private final TypeRepository typeRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final AccountService accountService;
    private final AccountBillRepository accountBillRepository;
    private final DictionaryService dictionaryService;
    private final AccountGoalRepository accountGoalRepository;
    private final TransactionCategoryRepository transactionCategoryRepository;
    private final UserService userService;
    private AccountRepository accountRepository;

    private final Map<String, String> demoAccountsDescriptions = Map.ofEntries(
            Map.entry(AccountTypeEnum.SAVING.getName(), "Счет, который предполагает сбережение средств на " +
                    "определенную цель"),
            Map.entry(AccountTypeEnum.CREDIT.getName(), "Счет, который предназначается для учета кредитной " +
                    "задолженности перед банком по кредитному договору"),
            Map.entry(AccountTypeEnum.SALARY.getName(), "Счет, который позволяет распоряжаться денежными средствами, " +
                    "полученными в качестве заработной платы."),
            Map.entry(AccountTypeEnum.CUMULATIVE.getName(), "Счет, который предлагает не только хранения денежных " +
                    "средств, но и возможность снятия в любой момент."));


    @Override
    public List<Account> generateAccounts(Long userID) throws IOException {
        List<Account> accounts = new ArrayList<>();
        Random random = new Random();
        List<Currency> currencies = dictionaryService.getAllCurrencies();
        String mainCurrency = userService.getUserProfile(userID).getCurrency();
        for (Type type : typeRepository.findAll()) {
            Account account = new Account();
            account.setName(type.getName() + " демо счет");
            account.setDescription(demoAccountsDescriptions.get(type.getCode()));
            Instant date = Instant.now().minus(random.nextInt(43) + 1, ChronoUnit.DAYS);
            account.setCreatedAt(date);
            account.setUserId(userID);
            account.setLastTransaction(date);
            boolean isSaving = type.getCode().equals(AccountTypeEnum.SAVING.getName());
            if (isSaving) {
                Instant freezeDate = Instant.now().plus(random.nextInt(10) + 1, ChronoUnit.DAYS);
                account.setFreezeDate(freezeDate);
            }
            Account saved = accountService.createAccount(account);
            AccountType accountType = new AccountType();
            accountType.setType(type);
            Long accountId = saved.getId();
            accountType.setAccountId(accountId);
            accountType.setTypeId(type.getId());
            AccountType save = accountTypeRepository.save(accountType);
            saved.setAccountType(save);

            LinkedList<AccountBill> bills = new LinkedList<>();
            AccountBill mainBill = new AccountBill();
            Currency currency = currencies.get(random.nextInt(currencies.size()));
            mainBill.setCurrency(currency);
            currencies.remove(currency);
            mainBill.setBalance(0d);
            mainBill.setAccountId(accountId);
            boolean isCredit = type.getCode().equals(AccountTypeEnum.CREDIT.getName());
            if (isCredit) {
                double startBalance = -NumberUtil.round(150000 + random.nextDouble() * 1000000);
                mainBill.setStartBalance(startBalance);
                mainBill.setBalance(startBalance);
                double rate = random.nextDouble() * 10;
                if (rate == 0.0d) {
                    rate = random.nextDouble() * 10;
                }
                mainBill.setRate(rate);
                LocalDate plus = LocalDate.now().plus(6 + random.nextInt(14), ChronoUnit.MONTHS);
                mainBill.setMaturityDate(plus.atStartOfDay().toInstant(ZoneOffset.UTC));
            }
            bills.add(mainBill);
            if (!isCredit && random.nextBoolean()) {
                AccountBill secondBill = new AccountBill();
                Currency currencySecond = currencies.get(random.nextInt(currencies.size()));
                secondBill.setCurrency(currencySecond);
                secondBill.setBalance(0d);
                secondBill.setAccountId(accountId);
                bills.offerLast(secondBill);
            }
            saved.setAccountBills(bills);

            List<AccountBill> accountBills = accountService.createAccountBills(accountId, bills);
            account.setAccountBills(accountBills);

            if (type.getCode().equals(AccountTypeEnum.CUMULATIVE.getName())) {
                AccountGoal goal = new AccountGoal();
                goal.setAccountId(accountId);
                goal.setCompleted(false);
                goal.setDate(Instant.now().plus(random.nextInt(29) + 5, ChronoUnit.DAYS));
                goal.setName("Демо цель");
                goal.setValue(NumberUtil.round(850000 + random.nextDouble() * 1500000));
                AccountGoal save1 = accountGoalRepository.save(goal);
                account.setGoal(save1);
                saved.setGoal(save1);
            }

            List<TransactionCategory> categoriesExpense = transactionCategoryRepository.findAllByType("EXPENSES");
            List<TransactionCategory> categoriesIncome = transactionCategoryRepository.findAllByType("INCOME");
            for (AccountBill accountBill : bills) {
                double sum = NumberUtil.round(!isCredit ? 1000 + random.nextDouble() * 350000 :
                        mainBill.getStartBalance());
                if (!isCredit) accountService.addTransaction(accountBill, userID, true, sum,
                        categoriesIncome.get(random.nextInt(categoriesIncome.size())).getId(), saved.getCreatedAt());
                Instant transactionDate = account.getCreatedAt();
                Instant now = Instant.now();
                Calendar nowCal = getCalendar(now);
                while (transactionDate.isBefore(now)) {
                    if (!(isCredit && sum >= 0)) {
                        boolean completed = false;
                        Calendar dateTime = getCalendar(transactionDate);
                        dateTime.set(Calendar.HOUR_OF_DAY, 0);
                        while (!completed) {
                            int addHour = random.nextInt(9);
                            int dateHour = dateTime.get(Calendar.HOUR_OF_DAY);
                            if(sameDay(transactionDate, now)) {
                                int nowHour = nowCal.get(Calendar.HOUR_OF_DAY);
                                completed = dateHour + addHour >= nowHour;
                            } else
                            completed = dateTime.get(Calendar.HOUR_OF_DAY) + addHour >= 24;
                            if (!completed) {
                               dateTime.add(Calendar.HOUR_OF_DAY, addHour);
                                boolean plus = (sum <= 10000) || random.nextBoolean() || isSaving;
                                double value;
                                if (plus) {
                                    value = 500 + random.nextDouble() * 10000;
                                    value *= isCredit ? 1.5 : 1;
                                    sum += value;
                                } else {
                                    value = 2500 + random.nextDouble() * 6500;
                                    sum -= value;
                                }
                                long transactionId = plus ?
                                        categoriesIncome.get(random.nextInt(categoriesIncome.size())).getId() :
                                        categoriesExpense.get(random.nextInt(categoriesIncome.size())).getId();
                                if (accountService.getConvertedBalance(saved, mainCurrency).getValue() < 0 && !isCredit) {
                                    throw new RuntimeException("Баланс счета не может быть меньше 0!");
                                }
                                accountService.addTransaction(accountBill, userID, plus, NumberUtil.round(value),
                                        transactionId, Instant.from(dateTime.toInstant()));
                            }
                        }
                    }
                    transactionDate = transactionDate.plus(1, ChronoUnit.DAYS);
                }
            }
            accounts.add(saved);
        }
        return accounts;
    }

    private Calendar getCalendar(Instant instant) {
        Calendar calendar1 = GregorianCalendar.getInstance();
        calendar1.setTime(Date.from(instant));

        return calendar1;

    }

    private boolean sameDay(Instant instant1, Instant instant2) {
        return sameDay(getCalendar(instant1), getCalendar(instant2));
    }

    private boolean sameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

}
