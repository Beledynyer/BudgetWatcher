package com.example.p3t2_budgetwatcher;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AggregationAdapter.OnItemClickListener {
    private SMSProcessor smsProcessor;
    private TaggingSystem taggingSystem;
    private TransactionAggregator aggregator;
    private List<Transaction> transactions;
    private RecyclerView transactionList;
    private RecyclerView aggregationList;
    private TransactionAdapter transactionAdapter;
    private AggregationAdapter aggregationAdapter;
    private DatePicker startDatePicker, endDatePicker;
    private Button aggregateButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsProcessor = new SMSProcessor(this);
        taggingSystem = new TaggingSystem();
        aggregator = new TransactionAggregator();
        transactions = new ArrayList<>();

        transactionList = findViewById(R.id.transaction_list);
        aggregationList = findViewById(R.id.aggregation_list);
        startDatePicker = findViewById(R.id.start_date_picker);
        endDatePicker = findViewById(R.id.end_date_picker);
        aggregateButton = findViewById(R.id.aggregate_button);

        transactionAdapter = new TransactionAdapter(transactions);
        transactionList.setAdapter(transactionAdapter);
        transactionList.setLayoutManager(new LinearLayoutManager(this));

        aggregationAdapter = new AggregationAdapter(new ArrayList<>(), this);
        aggregationList.setAdapter(aggregationAdapter);
        aggregationList.setLayoutManager(new LinearLayoutManager(this));

        requestSmsPermission();
        aggregateButton.setOnClickListener(v -> performAggregation());

        // initially hide the aggregation view
        aggregationList.setVisibility(View.GONE);
    }

    private void processTransactions() {
        transactions = smsProcessor.processMessages();
        for (Transaction transaction : transactions) {
            taggingSystem.tagTransaction(transaction);
        }
        transactionAdapter.updateTransactions(transactions);
        transactionList.setVisibility(View.VISIBLE);
        aggregationList.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void performAggregation() {
        LocalDate startDate = LocalDate.of(startDatePicker.getYear(), startDatePicker.getMonth() + 1, startDatePicker.getDayOfMonth());
        LocalDate endDate = LocalDate.of(endDatePicker.getYear(), endDatePicker.getMonth() + 1, endDatePicker.getDayOfMonth());

        Map<String, AggregationResult> aggregations = aggregator.aggregateByTags(transactions, startDate, endDate);
        aggregationAdapter.updateAggregations(aggregations, startDate, endDate);

        // Show aggregation list and hide transaction list
        transactionList.setVisibility(View.GONE);
        aggregationList.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(String tags, LocalDate startDate, LocalDate endDate) {
        List<Transaction> filteredTransactions = aggregator.getTransactionsForTags(transactions, startDate, endDate, tags);
        transactionAdapter.updateTransactions(filteredTransactions);

        // Show transaction list and hide aggregation list
        transactionList.setVisibility(View.VISIBLE);
        aggregationList.setVisibility(View.GONE);
    }
    private static final int SMS_PERMISSION_CODE = 100;

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                //need permission
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    SMS_PERMISSION_CODE);
        }
        else{
            //permission already given
            processTransactions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                processTransactions();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}