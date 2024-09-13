package com.example.p3t2_budgetwatcher;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateView;
        private final TextView descriptionView;
        private final TextView amountView;
        private final TextView tagsView;

        public ViewHolder(View view) {
            super(view);
            dateView = view.findViewById(R.id.transaction_date);
            descriptionView = view.findViewById(R.id.transaction_description);
            amountView = view.findViewById(R.id.transaction_amount);
            tagsView = view.findViewById(R.id.transaction_tags);
        }
    }

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.transaction_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Transaction transaction = transactions.get(position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        viewHolder.dateView.setText(transaction.getDate().format(formatter));
        viewHolder.descriptionView.setText(transaction.getDescription());
        viewHolder.amountView.setText(String.format("R %.2f", transaction.getAmount()));
        viewHolder.tagsView.setText(String.join(", ", transaction.getTags()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions = newTransactions;
        notifyDataSetChanged();
    }
}