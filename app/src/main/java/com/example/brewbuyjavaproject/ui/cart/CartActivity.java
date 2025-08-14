package com.example.brewbuyjavaproject.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brewbuyjavaproject.Model.CartItem;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.utils.CartManager;

import java.math.BigDecimal;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemListener {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvTotalAmount, tvEmptyCart;
    private Button btnCheckout;
    private CartManager cartManager;
    private List<CartItem> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupToolbar();
        cartManager = new CartManager(this);
        cartManager.setOnCartUpdateListener(this::updateCartDisplay);

        setupRecyclerView();
        updateCartDisplay();
        setupClickListeners();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Shopping Cart");
        }

        recyclerView = findViewById(R.id.recyclerView);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        btnCheckout = findViewById(R.id.btnCheckout);
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

    private void setupRecyclerView() {
        cartItems = cartManager.getCartItems();
        cartAdapter = new CartAdapter(cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
    }

    private void updateCartDisplay() {
        cartItems = cartManager.getCartItems();
        cartAdapter.updateItems(cartItems);

        if (cartItems.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(false);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
            btnCheckout.setEnabled(true);
        }

        BigDecimal total = cartManager.getTotalPrice();
        tvTotalAmount.setText("$" + String.format("%.2f", total));
    }

    private void setupClickListeners() {
        btnCheckout.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                Intent intent = new Intent(this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        if (newQuantity <= 0) {
            cartManager.removeFromCart(item.getProduct().getId());
        } else {
            cartManager.updateQuantity(item.getProduct().getId(), newQuantity);
        }
        updateCartDisplay();
    }

    @Override
    public void onItemRemoved(CartItem item) {
        cartManager.removeFromCart(item.getProduct().getId());
        updateCartDisplay();
        Toast.makeText(this, "Item removed from cart", Toast.LENGTH_SHORT).show();
    }
}