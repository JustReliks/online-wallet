package ru.onlinewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.onlinewallet.entity.account.Currency;
import ru.onlinewallet.entity.account.Type;
import ru.onlinewallet.repo.account.CurrencyRepository;
import ru.onlinewallet.repo.account.TypeRepository;
import ru.onlinewallet.service.DictionaryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final CurrencyRepository currencyRepository;
    private final TypeRepository typeRepository;

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public Currency getCurrency(String currName) {
        return currencyRepository.findByShortName(currName);
    }

    @Override
    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

}
