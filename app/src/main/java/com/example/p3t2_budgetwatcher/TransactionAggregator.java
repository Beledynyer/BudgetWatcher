package com.example.p3t2_budgetwatcher;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionAggregator {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Map<String, Double> aggregateByPeriod(List<Transaction> transactions, LocalDate startDate, LocalDate endDate, String period) {
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(
                        t -> getPeriodKey(t.getDate(), period),
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPeriodKey(LocalDate date, String period) {
        switch (period.toLowerCase()) {
            case "week":
                return "Week " + date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
            case "month":
                return date.getMonth().toString();
            case "year":
                return String.valueOf(date.getYear());
            default:
                return date.toString();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Transaction> getTransactionsForTag(List<Transaction> transactions, LocalDate startDate, LocalDate endDate, String tag) {
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
                .filter(t -> t.hasTag(tag))
                .collect(Collectors.toList());
    }
}
