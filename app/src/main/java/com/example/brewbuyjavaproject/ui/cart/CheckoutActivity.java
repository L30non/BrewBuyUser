// app/src/main/java/com/example/brewbuyjavaproject/ui/cart/CheckoutActivity.java
package com.example.brewbuyjavaproject.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.brewbuyjavaproject.Model.CartItem;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.utils.CartManager;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private EditText etShippingAddress, etCardNumber, etCardExpiry, etCardCVV;
    private RadioGroup rgPaymentMethod;
    private TextView tvSubtotal, tvShipping, tvTotal;
    private Button btnPlaceOrder;
    private CartManager cartManager;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupToolbar();
        cartManager = new CartManager(this);
        cartItems = cartManager.getCartItems();

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        calculateTotals();
        setupClickListeners();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Checkout");
        }

        etShippingAddress = findViewById(R.id.etShippingAddress);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardExpiry = findViewById(R.id.etCardExpiry);
        etCardCVV = findViewById(R.id.etCardCVV);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTotal = findViewById(R.id.tvTotal);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void calculateTotals() {
        BigDecimal subtotal = cartManager.getTotalPrice();
        BigDecimal shipping = new BigDecimal("5.00"); // Fixed shipping cost
        BigDecimal total = subtotal.add(shipping);

        tvSubtotal.setText("$" + String.format("%.2f", subtotal));
        tvShipping.setText("$" + String.format("%.2f", shipping));
        tvTotal.setText("$" + String.format("%.2f", total));
    }

    private void setupClickListeners() {
        btnPlaceOrder.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String shippingAddress = etShippingAddress.getText().toString().trim();
        int selectedPaymentMethod = rgPaymentMethod.getCheckedRadioButtonId();

        if (shippingAddress.isEmpty()) {
            etShippingAddress.setError("Shipping address is required");
            etShippingAddress.requestFocus();
            return;
        }

        if (selectedPaymentMethod == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod = "";
        if (selectedPaymentMethod == R.id.rbCreditCard) {
            paymentMethod = "Credit Card";
            if (!validateCardDetails()) {
                return;
            }
        } else if (selectedPaymentMethod == R.id.rbCashOnDelivery) {
            paymentMethod = "Cash on Delivery";
        }

        // Process order (in a real app, you would make an API call here)
        processOrder(shippingAddress, paymentMethod);
    }

    private boolean validateCardDetails() {
        String cardNumber = etCardNumber.getText().toString().trim();
        String cardExpiry = etCardExpiry.getText().toString().trim();
        String cardCVV = etCardCVV.getText().toString().trim();

        if (cardNumber.isEmpty()) {
            etCardNumber.setError("Card number is required");
            etCardNumber.requestFocus();
            return false;
        }

        if (cardExpiry.isEmpty()) {
            etCardExpiry.setError("Expiry date is required");
            etCardExpiry.requestFocus();
            return false;
        }

        if (cardCVV.isEmpty()) {
            etCardCVV.setError("CVV is required");
            etCardCVV.requestFocus();
            return false;
        }

        // Simple validation (in real app, use proper card validation)
        if (cardNumber.length() < 16) {
            etCardNumber.setError("Invalid card number");
            etCardNumber.requestFocus();
            return false;
        }

        return true;
    }

    private void processOrder(String shippingAddress, String paymentMethod) {
        // Show loading
        btnPlaceOrder.setEnabled(false);
        btnPlaceOrder.setText("Processing...");

        // Simulate order processing
        new android.os.Handler().postDelayed(() -> {
            // Clear cart
            cartManager.clearCart();

            // Navigate to success screen
            Intent intent = new Intent(this, OrderSuccessActivity.class);
            intent.putExtra("total_amount", cartManager.getTotalPrice().add(new BigDecimal("5.00")).toString());
            intent.putExtra("shipping_address", shippingAddress);
            intent.putExtra("payment_method", paymentMethod);
            startActivity(intent);
            finish();
        }, 2000);
    }
}