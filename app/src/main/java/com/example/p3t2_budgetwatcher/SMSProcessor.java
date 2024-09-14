package com.example.p3t2_budgetwatcher;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSProcessor {
    private Context context;
    private List<String> financialInstitutions;
    private List<Pattern> messagePatterns;

    public SMSProcessor(Context context) {
        this.context = context;
        initializeFinancialInstitutions();
        initializeMessagePatterns();
    }

    private void initializeFinancialInstitutions() {
        financialInstitutions = new ArrayList<>();
        financialInstitutions.add("Absa");
        financialInstitutions.add("Capitec");
        financialInstitutions.add("FNB");
    }

    private void initializeMessagePatterns() {
        messagePatterns = new ArrayList<>();
        messagePatterns.add(Pattern.compile("(\\w+): (\\w+\\d+), (\\w+(?:\\s+\\w+)?), (\\d{2}/\\d{2}/\\d{4}) .* - (.+?), R([\\-\\d.]+),\\s*Available R([\\d.]+)\\. Help \\d+; .* \\d+"));
    }


    public List<Transaction> processMessages() {
        List<Transaction> transactions = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/");
        String[] projection = new String[]{"body"};

        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String messageBody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    Transaction transaction = extractTransaction(messageBody);
                    if (transaction != null) {
                        transactions.add(transaction);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    //uses regular expressions to extract relevant information.
    private Transaction extractTransaction(String messageBody) {
        for (Pattern pattern : messagePatterns) {
            Matcher matcher = pattern.matcher(messageBody);
            if (matcher.find()) {
                String institution = matcher.group(1);
                if (financialInstitutions.contains(institution)) {
                    String account = matcher.group(2);
                    String type = matcher.group(3);
                    String date = matcher.group(4);
                    String description = matcher.group(5);
                    double amount = Double.parseDouble(matcher.group(6));
                    return new Transaction(institution, account, type, date, description, amount);
                }
            }
        }
        return null;
    }
}