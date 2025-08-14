// app/src/main/java/com/example/brewbuyjavaproject/ui/search/SearchActivity.java
package com.example.brewbuyjavaproject.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.Model.Product;
import com.example.brewbuyjavaproject.network.ApiService;
import com.example.brewbuyjavaproject.network.RetrofitClient;
import com.example.brewbuyjavaproject.ui.products.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {
    private static final String TAG = "SearchActivity";
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private EditText etSearch;
    private ImageButton btnClear;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "SearchActivity onCreate");

        initViews();
        setupRecyclerView();

        // Set up search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Show/hide clear button based on text length
                btnClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                
                // Perform search with a delay to avoid too many API calls
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set up clear button
        btnClear.setOnClickListener(v -> {
            etSearch.setText("");
            productList.clear();
            productAdapter.notifyDataSetChanged();
            showEmptyState(false);
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        btnClear = findViewById(R.id.btnClear);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
    }

    private void setupRecyclerView() {
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
    }

    private void performSearch(String keyword) {
        if (keyword.trim().isEmpty()) {
            // Clear the list if search is empty
            productList.clear();
            productAdapter.notifyDataSetChanged();
            showEmptyState(false);
            return;
        }

        // Show loading indicator
        showLoading(true);
        
        // Perform the search
        searchProducts(keyword);
    }

    private void searchProducts(String keyword) {
        Log.d(TAG, "Searching for products with keyword: " + keyword);
        
        ApiService apiService = RetrofitClient.getApiService(this);
        // Use getAllProducts instead of searchProducts since there's no search endpoint
        Call<List<Product>> call = apiService.getAllProducts();
        
        Log.d(TAG, "API call prepared for fetching all products for search");
        
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                showLoading(false);
                Log.d(TAG, "Products response received. Response code: " + response.code());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Products response successful");
                    if (response.body() != null) {
                        // Filter products client-side based on keyword
                        List<Product> allProducts = response.body();
                        List<Product> filteredProducts = new ArrayList<>();
                        
                        String lowerCaseKeyword = keyword.toLowerCase().trim();
                        for (Product product : allProducts) {
                            if (product.getName().toLowerCase().contains(lowerCaseKeyword) ||
                                product.getDescription().toLowerCase().contains(lowerCaseKeyword)) {
                                filteredProducts.add(product);
                            }
                        }
                        
                        Log.d(TAG, "Found " + allProducts.size() + " total products, " + filteredProducts.size() + " matching the search");
                        productList.clear();
                        productList.addAll(filteredProducts);
                        productAdapter.notifyDataSetChanged();
                        showEmptyState(productList.isEmpty());
                    } else {
                        Log.e(TAG, "Products response body is null");
                        showError("No data received. Please try again.");
                        showEmptyState(true);
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Products fetch failed. Response code: " + response.code() + ", Error: " + errorBody);
                        showError("Search failed: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                        showError("Search failed. Code: " + response.code());
                    }
                    showEmptyState(true);
                }
            }
            
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Products fetch network error", t);
                showError("Network error: " + t.getMessage());
                showEmptyState(true);
            }
        });
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyState(boolean show) {
        if (show) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showError(String message) {
        Log.e(TAG, "Error: " + message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProductClick(Product product) {
        // Handle product click - for now just show a toast
        Toast.makeText(this, "Product clicked: " + product.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddToCartClick(Product product) {
        // Handle add to cart click - for now just show a toast
        Toast.makeText(this, "Added to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
    }
}