package com.example.brewbuyjavaproject.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brewbuyjavaproject.Model.CartItem;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.utils.CartManager;

import java.math.BigDecimal;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView tvTotalAmount, tvEmptyCart;
    private Button btnCheckout;
    private CartManager cartManager;
    private List<CartItem> cartItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initViews(view);
        cartManager = new CartManager(requireContext());
        cartManager.setOnCartUpdateListener(this::updateCartDisplay);

        setupRecyclerView();
        updateCartDisplay();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart);
        btnCheckout = view.findViewById(R.id.btnCheckout);
    }

    private void setupRecyclerView() {
        cartItems = cartManager.getCartItems();
        cartAdapter = new CartAdapter(cartItems, this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(cartAdapter);
        }
    }

    private void updateCartDisplay() {
        cartItems = cartManager.getCartItems();
        if (cartAdapter != null) {
            cartAdapter.updateItems(cartItems);
        }

        if (cartItems != null && tvEmptyCart != null && recyclerView != null && btnCheckout != null) {
            if (cartItems.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvEmptyCart.setVisibility(View.VISIBLE);
                btnCheckout.setEnabled(false);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                tvEmptyCart.setVisibility(View.GONE);
                btnCheckout.setEnabled(true);
            }
        }

        if (cartManager != null && tvTotalAmount != null) {
            BigDecimal total = cartManager.getTotalPrice();
            tvTotalAmount.setText("$" + String.format("%.2f", total));
        }
    }

    private void setupClickListeners() {
        if (btnCheckout != null) {
            btnCheckout.setOnClickListener(v -> {
                if (cartItems != null && !cartItems.isEmpty()) {
                    Intent intent = new Intent(requireContext(), CheckoutActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        if (cartManager != null && item != null && item.getProduct() != null) {
            if (newQuantity <= 0) {
                cartManager.removeFromCart(item.getProduct().getId());
            } else {
                cartManager.updateQuantity(item.getProduct().getId(), newQuantity);
            }
            updateCartDisplay();
        }
    }

    @Override
    public void onItemRemoved(CartItem item) {
        if (cartManager != null && item != null && item.getProduct() != null) {
            cartManager.removeFromCart(item.getProduct().getId());
            updateCartDisplay();
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Item removed from cart", Toast.LENGTH_SHORT).show();
            }
        }
    }
}