package ru.onlinewallet.service.impl;

import ru.onlinewallet.entity.account.statistics.AccountStatistics;
import ru.onlinewallet.service.StatisticsService;

public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public AccountStatistics getStatistics(Long accountId, Long days) {
        if (days == 1) {

        }
        return null;
    }
}
