package ru.onlinewallet.service;

import ru.onlinewallet.entity.account.statistics.AccountStatistics;

public interface StatisticsService {
    AccountStatistics getStatistics(Long accountId, Long days);
}
