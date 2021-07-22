package ru.onlinewallet.service.impl;

import org.springframework.stereotype.Service;
import ru.onlinewallet.entity.account.statistics.AccountStatistics;
import ru.onlinewallet.service.StatisticsService;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public AccountStatistics getStatistics(Long accountId, Long days) {
        if (days == 1) {

        }
        return null;
    }
}
