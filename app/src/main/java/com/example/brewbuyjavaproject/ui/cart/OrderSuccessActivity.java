// app/src/main/java/com/example/brewbuyjavaproject/ui/cart/OrderSuccessActivity.java
package com.example.brewbuyjavaproject.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.brewbuyjavaproject.MainActivity;
import com.example.brewbuyjavaproject.R;

public class OrderSuccessActivity extends AppCompatActivity {
    private TextView tvOrderId, tvTotalAmount, tvShippingAddress, tvPaymentMethod;
    private Button btnContinueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        initViews();
        setupToolbar();

        // Get order details from intent
        Long orderId = getIntent().getLongExtra("order_id", -1);
        String totalAmount = getIntent().getStringExtra("total_amount");
        String shippingAddress = getIntent().getStringExtra("shipping_address");
        String paymentMethod = getIntent().getStringExtra("payment_method");

        // Display order details
        tvOrderId.setText("ORD-" + orderId);
        tvTotalAmount.setText("$" + totalAmount);
        tvShippingAddress.setText(shippingAddress);
        tvPaymentMethod.setText(paymentMethod);

        setupClickListeners();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Order Success");
        }

        tvOrderId = findViewById(R.id.tvOrderId);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setupClickListeners() {
        btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}