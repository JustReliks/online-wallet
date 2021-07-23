package ru.onlinewallet.entity.account.statistics;

import lombok.Data;

@Data
public class AccountStatistics {

    private LineChart incomeLineChart;
    private CircleChart incomeCircleChart;
    private LineChart expenseLineChart;
    private CircleChart expenseCircleChart;

}
