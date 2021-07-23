package ru.onlinewallet.entity.account.statistics;


import lombok.Data;
import org.springframework.data.util.Pair;
import ru.onlinewallet.entity.account.TransactionCategory;

import java.util.HashMap;
import java.util.List;

@Data
public class CircleChart {


    private HashMap<TransactionCategory, Double> rawData;

    private List<List<Object>> data;

}
