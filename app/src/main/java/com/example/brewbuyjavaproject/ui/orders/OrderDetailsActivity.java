package com.example.brewbuyjavaproject.ui.orders;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brewbuyjavaproject.Model.Order;
import com.example.brewbuyjavaproject.Model.OrderItem;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.network.OrderDataManager;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity implements OrderDataManager.OrderCallback {
    private TextView tvOrderId, tvOrderDate, tvOrderStatus, tvSubtotal, tvShipping, tvTotal;
    private RecyclerView recyclerOrderItems;
    private OrderItemAdapter orderItemAdapter;
    
    private OrderDataManager orderDataManager;
    private long orderId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        
        initViews();
        setupToolbar();
        
        // Get order ID from intent
        orderId = getIntent().getLongExtra("order_id", -1);
        if (orderId == -1) {
            finish();
            return;
        }
        
        setupRecyclerView();
        orderDataManager = new OrderDataManager(this);
        loadOrderDetails();
    }
    
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Order Details");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTotal = findViewById(R.id.tvTotal);
        recyclerOrderItems = findViewById(R.id.recycler_order_items);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupRecyclerView() {
        orderItemAdapter = new OrderItemAdapter(new ArrayList<>());
        recyclerOrderItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrderItems.setAdapter(orderItemAdapter);
    }
    
    private void loadOrderDetails() {
        orderDataManager.getOrderById(orderId, this);
    }
    
    @Override
    public void onSuccess(Order order) {
        displayOrderDetails(order);
    }
    
    @Override
    public void onError(String error) {
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }
    
    private void displayOrderDetails(Order order) {
        // Display order ID
        tvOrderId.setText("ORD-" + order.getId());
        
        // Display order date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateStr = order.getCreatedAt() != null ? sdf.format(order.getCreatedAt()) : "N/A";
        tvOrderDate.setText(dateStr);
        
        // Display order status
        tvOrderStatus.setText(order.getStatus() != null ? order.getStatus() : "N/A");
        
        // Display order items
        List<OrderItem> items = order.getItems();
        if (items != null) {
            orderItemAdapter = new OrderItemAdapter(items);
            recyclerOrderItems.setAdapter(orderItemAdapter);
        }
        
        // Calculate and display totals
        BigDecimal subtotal = calculateSubtotal(items);
        BigDecimal shipping = new BigDecimal("5.00"); // Fixed shipping cost
        BigDecimal total = subtotal.add(shipping);
        
        tvSubtotal.setText("$" + String.format("%.2f", subtotal));
        tvShipping.setText("$" + String.format("%.2f", shipping));
        tvTotal.setText("$" + String.format("%.2f", total));
    }
    
    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        BigDecimal subtotal = BigDecimal.ZERO;
        if (items != null) {
            for (OrderItem item : items) {
                if (item.getPrice() != null && item.getQuantity() > 0) {
                    subtotal = subtotal.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }
            }
        }
        return subtotal;
    }
}