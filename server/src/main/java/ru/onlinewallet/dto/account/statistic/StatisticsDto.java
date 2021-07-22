package ru.onlinewallet.dto.account.statistic;

import lombok.Data;

@Data
public class StatisticsDto {
    private LineChartDto incomeLineChart;
    private CircleChartDto incomeCircleChart;
    private LineChartDto expenseLineChart;
    private CircleChartDto expenseCircleChart;

}
