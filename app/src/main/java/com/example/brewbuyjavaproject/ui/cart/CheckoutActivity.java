// app/src/main/java/com/example/brewbuyjavaproject/ui/cart/CheckoutActivity.java
package com.example.brewbuyjavaproject.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.brewbuyjavaproject.Model.CartItem;
import com.example.brewbuyjavaproject.Model.Order;
import com.example.brewbuyjavaproject.Model.OrderItem;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.network.OrderDataManager;
import com.example.brewbuyjavaproject.utils.CartManager;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements OrderDataManager.OrderCallback {
    private static final String TAG = "CheckoutActivity";
    private EditText etShippingAddress, etCardNumber, etCardExpiry, etCardCVV;
    private RadioGroup rgPaymentMethod;
    private TextView tvSubtotal, tvShipping, tvTotal;
    private Button btnPlaceOrder;
    private CartManager cartManager;
    private List<CartItem> cartItems;
    private OrderDataManager orderDataManager;
    private BigDecimal totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupToolbar();
        cartManager = new CartManager(this);
        orderDataManager = new OrderDataManager(this);
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
        totalAmount = subtotal.add(shipping);

        tvSubtotal.setText("$" + String.format("%.2f", subtotal));
        tvShipping.setText("$" + String.format("%.2f", shipping));
        tvTotal.setText("$" + String.format("%.2f", totalAmount));
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
            paymentMethod = "CREDIT_CARD";
            if (!validateCardDetails()) {
                return;
            }
        } else if (selectedPaymentMethod == R.id.rbCashOnDelivery) {
            paymentMethod = "CASH_ON_DELIVERY";
        }

        // Create order object
        createOrder(shippingAddress, paymentMethod);
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

    private void createOrder(String shippingAddress, String paymentMethod) {
        // Show loading
        btnPlaceOrder.setEnabled(false);
        btnPlaceOrder.setText("Processing...");

        // Create order items from cart items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProduct().getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(orderItem);
        }

        // Create order object
        Order order = new Order();
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        // Note: userId will be handled by the backend based on the JWT token
        // Shipping address and payment method could be added to the order model if needed
        // For now, we'll pass them as parameters to the success screen

        Log.d(TAG, "Creating order object: " + order.toString());
        
        // Make API call to create order
        orderDataManager.createOrder(order, this);
    }

    @Override
    public void onSuccess(Order order) {
        Log.d(TAG, "Order created successfully: " + order.getId());
        // Clear cart
        cartManager.clearCart();

        // Navigate to success screen
        Intent intent = new Intent(this, OrderSuccessActivity.class);
        intent.putExtra("order_id", order.getId());
        intent.putExtra("total_amount", totalAmount.toString());
        intent.putExtra("shipping_address", etShippingAddress.getText().toString().trim());
        intent.putExtra("payment_method", getSelectedPaymentMethodName());
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "Error creating order: " + error);
        // Hide loading
        btnPlaceOrder.setEnabled(true);
        btnPlaceOrder.setText("Place Order");

        // Show error message
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }

    private String getSelectedPaymentMethodName() {
        int selectedPaymentMethod = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedPaymentMethod == R.id.rbCreditCard) {
            return "Credit Card";
        } else if (selectedPaymentMethod == R.id.rbCashOnDelivery) {
            return "Cash on Delivery";
        }
        return "";
    }
}