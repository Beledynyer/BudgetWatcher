package com.example.p3t2_budgetwatcher;

import android.annotation.SuppressLint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Transaction {
    private String institution;
    private String account;
    private String type;
    private LocalDate date;
    private String description;

    public String getInstitution() {
        return institution;
    }

    public String getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    private double amount;
    private Set<String> tags;

    @SuppressLint("NewApi")
    public Transaction(String institution, String account, String type, String date, String description, double amount) {
        this.institution = institution;
        this.account = account;
        this.type = type;
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yy"));
        this.description = description;
        this.amount = amount;
        this.tags = new HashSet<>();
    }

    // Getters and setters

    public void addTag(String tag) {
        tags.add(tag.toLowerCase());
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag.toLowerCase());
    }

    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%s - %s: %.2f - %s", date, type, amount, description);
    }
}