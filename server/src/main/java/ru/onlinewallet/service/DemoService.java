package ru.onlinewallet.service;

import ru.onlinewallet.entity.account.Account;

import java.io.IOException;
import java.util.List;

public interface DemoService {

    List<Account> generateAccounts(Long userID) throws IOException;
}
