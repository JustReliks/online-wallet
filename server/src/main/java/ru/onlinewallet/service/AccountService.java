package ru.onlinewallet.service;

import org.xml.sax.SAXException;
import ru.onlinewallet.entity.ConvertedBalance;
import ru.onlinewallet.entity.account.Account;
import ru.onlinewallet.entity.account.AccountBill;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public interface AccountService {

    Account createAccount(Account account);

    List<AccountBill> createAccountBills(Long id, List<AccountBill> collect);

    List<Account> getAll(Long userId);

    AccountBill addTransaction(AccountBill accountBill, boolean isPlus, double value);

    Double convertCurrencies(double value, String from, String to) throws IOException;

    ConvertedBalance getConvertedBalance(Long id, String currency) throws IOException;
}
