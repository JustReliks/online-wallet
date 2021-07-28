package ru.onlinewallet.entity.account.statistics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LineChart implements Cloneable, Serializable {

    private List<String> categories;
    private List<Double> seriesData;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
