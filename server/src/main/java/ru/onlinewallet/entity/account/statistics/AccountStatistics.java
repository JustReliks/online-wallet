package ru.onlinewallet.entity.account.statistics;

import lombok.Data;

import java.util.List;

@Data
public class AccountStatistics implements Cloneable{

    private LineChart incomeLineChart;
    private CircleChart incomeCircleChart;
    private LineChart expenseLineChart;
    private CircleChart expenseCircleChart;
    private LineChart moneyLineChart;
    private Integer allTransactions;
    private List<Double> incomes;
    private List<Double> expenses;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
