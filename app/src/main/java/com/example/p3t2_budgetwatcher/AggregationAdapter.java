package com.example.p3t2_budgetwatcher;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggregationAdapter extends RecyclerView.Adapter<AggregationAdapter.ViewHolder> {

    private List<Map.Entry<String, AggregationResult>> aggregations;
    private OnItemClickListener listener;
    private LocalDate startDate, endDate;

    //using interface for onItemClick (best practice)
    public interface OnItemClickListener {
        void onItemClick(String tags, LocalDate startDate, LocalDate endDate);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateRangeView;
        private final TextView tagsView;
        private final TextView amountView;

        public ViewHolder(View view) {
            super(view);
            dateRangeView = view.findViewById(R.id.aggregation_date_range);
            tagsView = view.findViewById(R.id.aggregation_tags);
            amountView = view.findViewById(R.id.aggregation_amount);
        }
    }

    public AggregationAdapter(List<Map.Entry<String, AggregationResult>> aggregations, OnItemClickListener listener) {
        this.aggregations = aggregations;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.aggregation_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Map.Entry<String, AggregationResult> aggregation = aggregations.get(position);
        String tags = aggregation.getKey();
        AggregationResult result = aggregation.getValue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateRange = startDate.format(formatter) + " - " + endDate.format(formatter);

        viewHolder.dateRangeView.setText(dateRange);
        viewHolder.tagsView.setText(tags);
        viewHolder.amountView.setText(String.format("R %.2f (%d transactions)", result.getAmount(), result.getCount()));

        viewHolder.itemView.setOnClickListener(v -> listener.onItemClick(tags, startDate, endDate));
    }

    @Override
    public int getItemCount() {
        return aggregations.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAggregations(Map<String, AggregationResult> newAggregations, LocalDate startDate, LocalDate endDate) {
        this.aggregations = new ArrayList<>(newAggregations.entrySet());
        this.startDate = startDate;
        this.endDate = endDate;
        notifyDataSetChanged();
    }
}