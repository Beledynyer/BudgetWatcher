package com.example.p3t2_budgetwatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggregationAdapter extends RecyclerView.Adapter<AggregationAdapter.ViewHolder> {

    private List<Map.Entry<String, Double>> aggregations;
    private OnItemClickListener listener;
    private LocalDate startDate, endDate;

    public interface OnItemClickListener {
        void onItemClick(String tag, LocalDate startDate, LocalDate endDate);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView periodView;
        private final TextView amountView;

        public ViewHolder(View view) {
            super(view);
            periodView = view.findViewById(R.id.aggregation_period);
            amountView = view.findViewById(R.id.aggregation_amount);
        }
    }

    public AggregationAdapter(Map<String, Double> aggregations, OnItemClickListener listener) {
        this.aggregations = new ArrayList<>(aggregations.entrySet());
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.aggregation_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Map.Entry<String, Double> aggregation = aggregations.get(position);
        viewHolder.periodView.setText(aggregation.getKey());
        viewHolder.amountView.setText(String.format("R %.2f", aggregation.getValue()));

        viewHolder.itemView.setOnClickListener(v -> listener.onItemClick(aggregation.getKey(), startDate, endDate));
    }

    @Override
    public int getItemCount() {
        return aggregations.size();
    }

    public void updateAggregations(Map<String, Double> newAggregations, LocalDate startDate, LocalDate endDate) {
        this.aggregations = new ArrayList<>(newAggregations.entrySet());
        this.startDate = startDate;
        this.endDate = endDate;
        notifyDataSetChanged();
    }
}