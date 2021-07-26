package ru.onlinewallet.dto.account.statistic;

import lombok.Data;
import ru.onlinewallet.entity.account.statistics.AccountStatistics;

import java.util.List;

@Data
public class StatisticsDto {
    private LineChartDto incomeLineChart;
    private CircleChartDto incomeCircleChart;
    private LineChartDto expenseLineChart;
    private CircleChartDto expenseCircleChart;
    private LineChartDto moneyLineChart;
    private Integer allTransactions;
    private List<Double> incomes;
    private List<Double> expenses;

    public static StatisticsDto toDto(AccountStatistics accountStatistics) {
        StatisticsDto dto = new StatisticsDto();
        dto.setExpenseLineChart(LineChartDto.toDto(accountStatistics.getExpenseLineChart()));
        dto.setIncomeLineChart(LineChartDto.toDto(accountStatistics.getIncomeLineChart()));
        dto.setIncomeCircleChart(CircleChartDto.toDto(accountStatistics.getIncomeCircleChart()));
        dto.setExpenseCircleChart(CircleChartDto.toDto(accountStatistics.getExpenseCircleChart()));
        dto.setMoneyLineChart(LineChartDto.toDto(accountStatistics.getMoneyLineChart()));
        dto.setAllTransactions(accountStatistics.getAllTransactions());
        dto.setExpenses(accountStatistics.getExpenses());
        dto.setIncomes(accountStatistics.getIncomes());

        return dto;
    }

}
