package com.example.p3t2_budgetwatcher;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** @noinspection Since15*/
public class TransactionAggregator {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Map<String, AggregationResult> aggregateByTags(List<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .flatMap(t -> t.getTags().stream().map(tag -> new Object[]{tag, t}))
                .collect(Collectors.groupingBy(
                        arr -> (String) arr[0],
                        Collectors.collectingAndThen(
                                Collectors.summarizingDouble(arr -> ((Transaction) arr[1]).getAmount()),
                                summary -> new AggregationResult(summary.getSum(), summary.getCount())
                        )
                ));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Transaction> getTransactionsForTags(List<Transaction> transactions, LocalDate startDate, LocalDate endDate, String tags) {
        List<String> tagList = List.of(tags.split(","));
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .filter(t -> t.getTags().stream().anyMatch(tagList::contains))
                .collect(Collectors.toList());
    }
}

class AggregationResult {
    private double amount;
    private long count;

    public AggregationResult(double amount, long count) {
        this.amount = amount;
        this.count = count;
    }

    public double getAmount() {
        return amount;
    }

    public long getCount() {
        return count;
    }
}