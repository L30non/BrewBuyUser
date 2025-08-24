package com.example.brewbuyjavaproject.ui.orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.brewbuyjavaproject.Model.Order;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.network.OrderDataManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment implements OrderDataManager.OrderListCallback, OrderListAdapter.OnOrderClickListener {
    private RecyclerView recyclerOrders;
    private ProgressBar progressBar;
    private TextView textError;
    private TextView textEmpty;
    
    private OrderListAdapter adapter;
    private OrderDataManager orderDataManager;
    
    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDataManager = new OrderDataManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        
        initViews(view);
        setupRecyclerView();
        loadOrders();
        
        return view;
    }
    
    private void initViews(View view) {
        recyclerOrders = view.findViewById(R.id.recycler_orders);
        progressBar = view.findViewById(R.id.progress_bar);
        textError = view.findViewById(R.id.text_error);
        textEmpty = view.findViewById(R.id.text_empty);
    }
    
    private void setupRecyclerView() {
        adapter = new OrderListAdapter(new ArrayList<>());
        adapter.setOnOrderClickListener(this);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerOrders.setAdapter(adapter);
    }
    
    private void loadOrders() {
        showLoading(true);
        orderDataManager.getMyOrders(this);
    }
    
    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerOrders.setVisibility(show ? View.GONE : View.VISIBLE);
        textError.setVisibility(View.GONE);
        textEmpty.setVisibility(View.GONE);
    }
    
    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        recyclerOrders.setVisibility(View.GONE);
        textError.setVisibility(View.VISIBLE);
        textEmpty.setVisibility(View.GONE);
        
        // Show a Snackbar with the error message
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }
    
    private void showEmpty() {
        progressBar.setVisibility(View.GONE);
        recyclerOrders.setVisibility(View.GONE);
        textError.setVisibility(View.GONE);
        textEmpty.setVisibility(View.VISIBLE);
    }
    
    private void showOrders(List<Order> orders) {
        progressBar.setVisibility(View.GONE);
        recyclerOrders.setVisibility(View.VISIBLE);
        textError.setVisibility(View.GONE);
        textEmpty.setVisibility(orders.isEmpty() ? View.VISIBLE : View.GONE);
        
        adapter.setOrders(orders);
    }

    @Override
    public void onSuccess(List<Order> orders) {
        if (isAdded()) { // Check if fragment is still attached to activity
            showOrders(orders);
        }
    }

    @Override
    public void onError(String error) {
        if (isAdded()) { // Check if fragment is still attached to activity
            showError(error);
        }
    }

    @Override
    public void onOrderClick(Order order) {
        // Navigate to order details screen
        Intent intent = new Intent(requireContext(), OrderDetailsActivity.class);
        intent.putExtra("order_id", order.getId());
        startActivity(intent);
    }
}