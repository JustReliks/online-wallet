package ru.onlinewallet.entity.account.statistics;

import lombok.Data;

import java.util.List;

@Data
public class LineChart {

    private List<String> categories;
    private List<Double> seriesData;

}
