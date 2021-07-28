package ru.onlinewallet.entity.account.statistics;


import lombok.Data;
import org.springframework.data.util.Pair;
import ru.onlinewallet.entity.account.TransactionCategory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@Data
public class CircleChart implements Cloneable, Serializable {


    private HashMap<TransactionCategory, Double> rawData;

    private List<List<Object>> data;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
