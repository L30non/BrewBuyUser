// app/src/main/java/com/example/brewbuyjavaproject/MainActivity.java
package com.example.brewbuyjavaproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brewbuyjavaproject.network.ApiService;
import com.example.brewbuyjavaproject.network.RetrofitClient;
import com.example.brewbuyjavaproject.ui.auth.LoginActivity;
import com.example.brewbuyjavaproject.ui.cart.CartActivity;
import com.example.brewbuyjavaproject.ui.products.ProductListActivity;
import com.example.brewbuyjavaproject.utils.CartManager;
import com.example.brewbuyjavaproject.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private CartManager cartManager;
    private TextView tvCartBadge;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in first
        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            // Redirect to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

         testApiConnection(); // Uncomment this line to test API connection

        initViews();
        setupHeader();
        setupBottomNavigation();
        setupCategoryCards();
        setupFeaturedProducts();
        updateCartBadge();
    }

    private void initViews() {
        cartManager = new CartManager(this);
        tvCartBadge = findViewById(R.id.tvCartBadge);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setupHeader() {
        // Find header views from the included layout
        View header = findViewById(R.id.header);
        ImageView ivSearch = header.findViewById(R.id.ivSearch);
        ImageView ivNotifications = header.findViewById(R.id.ivNotifications);
        ImageView ivCart = header.findViewById(R.id.ivCart);
        ImageView ivProfile = header.findViewById(R.id.ivProfile);
        TextView tvViewAllProducts = findViewById(R.id.tvViewAllProducts);

        // Set click listeners for header icons
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchClick();
            }
        });

        ivNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNotificationsClick();
            }
        });

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCartClick();
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleProfileClick();
            }
        });

        tvViewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleViewAllProductsClick();
            }
        });
    }

    private void handleSearchClick() {
        Intent intent = new Intent(this, com.example.brewbuyjavaproject.ui.search.SearchActivity.class);
        startActivity(intent);
    }

    private void handleNotificationsClick() {
        Toast.makeText(this, "Notifications will be implemented", Toast.LENGTH_SHORT).show();
        // TODO: Implement notifications functionality
    }

    private void handleCartClick() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    private void handleProfileClick() {
        showProfileMenu();
    }

    private void handleViewAllProductsClick() {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    private void showProfileMenu() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Profile Options")
                .setMessage("Choose an option")
                .setPositiveButton("Logout", (dialog, which) -> {
                    logoutUser();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void logoutUser() {
        sessionManager.logoutUser();
        cartManager.clearCart();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    // Already on home screen
                    return true;
                } else if (itemId == R.id.nav_search) {
                    handleSearchClick();
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    handleCartClick();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    handleProfileClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupCategoryCards() {
        CardView cardCoffeeBeans = findViewById(R.id.cardCoffeeBeans);
        CardView cardCoffeeMachines = findViewById(R.id.cardCoffeeMachines);
        CardView cardAccessories = findViewById(R.id.cardAccessories);
        CardView cardMerchandise = findViewById(R.id.cardMerchandise);

        cardCoffeeBeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Coffee Beans clicked", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to coffee beans category
            }
        });

        cardCoffeeMachines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Coffee Machines clicked", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to coffee machines category
            }
        });

        cardAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Accessories clicked", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to accessories category
            }
        });

        cardMerchandise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Merchandise clicked", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to merchandise category
            }
        });
    }

    private void setupFeaturedProducts() {
        RecyclerView rvFeaturedProducts = findViewById(R.id.rvFeaturedProducts);
        rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Setup featured products adapter here
        // For now, just create an empty adapter or mock data
    }

    private void updateCartBadge() {
        try {
            int cartCount = cartManager.getCartItemCount();
            if (cartCount > 0) {
                tvCartBadge.setText(String.valueOf(cartCount));
                tvCartBadge.setVisibility(View.VISIBLE);
            } else {
                tvCartBadge.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            tvCartBadge.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    // *** CHANGED: Add this method to MainActivity for debugging ***
    private void testApiConnection() {
        try {
            ApiService apiService = RetrofitClient.getApiService(this);
            Call<String> call = apiService.testConnection();

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d("MainActivity", "API Test Success: " + response.body());
                        Toast.makeText(MainActivity.this, "API Connected: " + response.body(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("MainActivity", "API Test Failed: " + response.code());
                        Toast.makeText(MainActivity.this, "API Test Failed: " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("MainActivity", "API Test Network Error: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "API Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "API Test Exception", e);
        }
    }


}