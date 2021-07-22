package ru.onlinewallet.dto.account.statistic;

import lombok.Data;

import java.util.List;

@Data
public class LineChartDto {
    private List<String> categories;
    private List<Double> seriesData;
}
