package com.example.brewbuyjavaproject.ui.products;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.brewbuyjavaproject.Model.Product;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.network.ApiService;
import com.example.brewbuyjavaproject.network.RetrofitClient;
import com.example.brewbuyjavaproject.utils.CartManager;
import com.example.brewbuyjavaproject.utils.ImageUtils;

import java.io.ByteArrayInputStream;

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
                    Log.d(TAG, "Product details loaded: " + product.getName() + 
                          ", ID: " + product.getId() + 
                          ", ImageBase64: " + (product.getImageBase64() != null ? product.getImageBase64().length() + " chars" : "null") +
                          ", ImageType: " + product.getImageType());
                    displayProductDetails();
                } else {
                    Log.e(TAG, "Failed to load product details. Response code: " + response.code());
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

            // Load image using Base64 data
            loadProductImage();
        }
    }

    private void loadProductImage() {
        String base64Image = product.getImageBase64();
        
        if (base64Image != null && !base64Image.isEmpty()) {
            Log.d(TAG, "Product detail " + product.getId() + " has Base64 image data, length: " + base64Image.length());
            
            // Convert Base64 to stream for Glide
            ByteArrayInputStream imageStream = ImageUtils.base64ToStream(base64Image);
            
            if (imageStream != null) {
                Log.d(TAG, "Successfully converted Base64 to stream for product detail " + product.getId());
                Glide.with(this)
                        .load(imageStream)
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk cache for streams
                        .skipMemoryCache(true) // Skip memory cache to avoid issues with streams
                        .placeholder(R.drawable.ic_coffee)
                        .error(R.drawable.ic_coffee)
                        .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                                Log.e(TAG, "Glide failed to load image for product detail " + product.getId(), e);
                                return false; // Allow error drawable to be shown
                            }

                            @Override
                            public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, Target<android.graphics.drawable.Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d(TAG, "Glide successfully loaded image for product detail " + product.getId());
                                return false; // Allow drawable to be set
                            }
                        })
                        .into(ivProductImage);
            } else {
                Log.e(TAG, "Failed to convert Base64 to stream for product detail " + product.getId());
                // Fallback to placeholder if conversion fails
                ivProductImage.setImageResource(R.drawable.ic_coffee);
            }
        } else {
            Log.d(TAG, "Product detail " + product.getId() + " has no Base64 image data");
            // Use placeholder if no image data
            ivProductImage.setImageResource(R.drawable.ic_coffee);
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