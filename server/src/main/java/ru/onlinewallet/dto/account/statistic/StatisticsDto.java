package ru.onlinewallet.dto.account.statistic;

import lombok.Data;
import ru.onlinewallet.entity.account.statistics.AccountStatistics;

@Data
public class StatisticsDto {
    private LineChartDto incomeLineChart;
    private CircleChartDto incomeCircleChart;
    private LineChartDto expenseLineChart;
    private CircleChartDto expenseCircleChart;

    public static StatisticsDto toDto(AccountStatistics accountStatistics)
    {
        StatisticsDto dto = new StatisticsDto();
        dto.setExpenseLineChart(LineChartDto.toDto(accountStatistics.getExpenseLineChart()));
        dto.setIncomeLineChart(LineChartDto.toDto(accountStatistics.getIncomeLineChart()));
        dto.setIncomeCircleChart(CircleChartDto.toDto(accountStatistics.getIncomeCircleChart()));
        dto.setExpenseCircleChart(CircleChartDto.toDto(accountStatistics.getExpenseCircleChart()));

        return dto;
    }

}
