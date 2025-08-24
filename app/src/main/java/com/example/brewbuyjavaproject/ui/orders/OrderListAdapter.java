package com.example.brewbuyjavaproject.ui.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brewbuyjavaproject.Model.Order;
import com.example.brewbuyjavaproject.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {
    private List<Order> orders;
    private OnOrderClickListener listener;

    public OrderListAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView textOrderId;
        private TextView textOrderDate;
        private TextView textOrderTotal;
        private TextView textOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderId = itemView.findViewById(R.id.text_order_id);
            textOrderDate = itemView.findViewById(R.id.text_order_date);
            textOrderTotal = itemView.findViewById(R.id.text_order_total);
            textOrderStatus = itemView.findViewById(R.id.text_order_status);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(orders.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Order order) {
            textOrderId.setText("Order ID: " + order.getId());
            
            // Format date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateStr = "Order Date: " + (order.getCreatedAt() != null ? sdf.format(order.getCreatedAt()) : "N/A");
            textOrderDate.setText(dateStr);
            
            // Format total amount
            BigDecimal total = order.getTotalAmount();
            String totalStr = "Total: $" + (total != null ? total.toString() : "0.00");
            textOrderTotal.setText(totalStr);
            
            textOrderStatus.setText("Status: " + (order.getStatus() != null ? order.getStatus() : "N/A"));
        }
    }

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }
}