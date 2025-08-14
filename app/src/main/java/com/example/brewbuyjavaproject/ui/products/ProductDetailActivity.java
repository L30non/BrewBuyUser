package com.example.brewbuyjavaproject.ui.products;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.brewbuyjavaproject.Model.Product;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.network.ApiService;
import com.example.brewbuyjavaproject.network.RetrofitClient;
import com.example.brewbuyjavaproject.utils.CartManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProductDetailActivity";
    private Long productId;
    private Product product;
    private ImageView ivProductImage;
    private TextView tvProductName, tvProductPrice, tvProductDescription, tvProductCategory;
    private Button btnAddToCart, btnBuyNow;
    private ProgressBar progressBar;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();
        setupToolbar();
        cartManager = new CartManager(this);

        productId = getIntent().getLongExtra("product_id", -1);
        if (productId != -1) {
            loadProductDetails();
        } else {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupClickListeners();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvProductCategory = findViewById(R.id.tvProductCategory);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Product Details");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void loadProductDetails() {
        showLoading(true);

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<Product> call = apiService.getProductById(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    product = response.body();
                    displayProductDetails();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Failed to load product details", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Failed to load product details", t);
                Toast.makeText(ProductDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void displayProductDetails() {
        if (product != null) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText("$" + product.getPrice());
            tvProductDescription.setText(product.getDescription());
            tvProductCategory.setText(product.getCategory() != null ? product.getCategory() : "Coffee");

            // Load image
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(product.getImageUrl())
                        .placeholder(R.drawable.ic_coffee)
                        .error(R.drawable.ic_coffee)
                        .into(ivProductImage);
            } else {
                ivProductImage.setImageResource(R.drawable.ic_coffee);
            }
        }
    }

    private void setupClickListeners() {
        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                cartManager.addToCart(product);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuyNow.setOnClickListener(v -> {
            if (product != null) {
                cartManager.addToCart(product);
                // Navigate to cart
                // startActivity(new Intent(this, CartActivity.class));
            }
        });
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}