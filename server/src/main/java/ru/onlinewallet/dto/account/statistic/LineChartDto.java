package ru.onlinewallet.dto.account.statistic;

import lombok.Data;
import ru.onlinewallet.entity.account.statistics.LineChart;

import java.util.List;

@Data
public class LineChartDto {
    private List<String> categories;
    private List<Double> seriesData;

    public static LineChartDto toDto(LineChart chart)
    {
        LineChartDto dto = new LineChartDto();
        dto.setCategories(chart.getCategories());
        dto.setSeriesData(chart.getSeriesData());

        return dto;
    }

}
