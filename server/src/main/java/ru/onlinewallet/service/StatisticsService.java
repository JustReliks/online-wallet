package ru.onlinewallet.service;

import ru.onlinewallet.entity.account.statistics.AccountStatistics;

import java.io.IOException;

public interface StatisticsService {
    AccountStatistics getStatistics(Long accountId, Long days) throws IOException;
}
