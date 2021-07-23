package ru.onlinewallet.dto.account.statistic;

import lombok.Data;
import org.springframework.data.util.Pair;
import ru.onlinewallet.entity.account.statistics.CircleChart;

import java.util.List;

@Data
public class CircleChartDto {

    private List<Pair<String, Double>> data;

    public static CircleChartDto toDto(CircleChart chart)
    {
        CircleChartDto dto = new CircleChartDto();
        dto.setData(chart.getData());

        return dto;
    }

}
