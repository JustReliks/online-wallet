package ru.onlinewallet.service;

import ru.onlinewallet.entity.account.Currency;

import java.util.List;

public interface DictionaryService {
    List<Currency> getAllCurrencies();

    Currency getCurrency(String currName);
}
