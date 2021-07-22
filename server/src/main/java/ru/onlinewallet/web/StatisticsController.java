package ru.onlinewallet.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.onlinewallet.dto.account.statistic.StatisticsDto;
import ru.onlinewallet.entity.account.statistics.AccountStatistics;
import ru.onlinewallet.service.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    private StatisticsDto getStatistics(@RequestParam("account") Long accountId, @RequestParam("days") Long days) {
        AccountStatistics statistics =  statisticsService.getStatistics(accountId, days);
        return null;
    }

}
