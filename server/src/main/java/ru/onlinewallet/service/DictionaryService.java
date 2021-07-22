package ru.onlinewallet.service;

import ru.onlinewallet.entity.account.Currency;
import ru.onlinewallet.entity.account.TransactionCategory;
import ru.onlinewallet.entity.account.Type;

import java.util.List;

public interface DictionaryService {
    List<Currency> getAllCurrencies();

    Currency getCurrency(String currName);

    List<Type> getAllTypes();

    List<TransactionCategory> getAllTransactionCategories();
}
