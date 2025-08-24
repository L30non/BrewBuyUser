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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brewbuyjavaproject.network.ApiService;
import com.example.brewbuyjavaproject.network.RetrofitClient;
import com.example.brewbuyjavaproject.ui.auth.LoginActivity;
import com.example.brewbuyjavaproject.ui.cart.CartFragment;
import com.example.brewbuyjavaproject.ui.orders.OrderFragment;
import com.example.brewbuyjavaproject.ui.products.ProductListActivity;
import com.example.brewbuyjavaproject.ui.profile.ProfileFragment;
import com.example.brewbuyjavaproject.ui.search.SearchFragment;
import com.example.brewbuyjavaproject.utils.CartManager;
import com.example.brewbuyjavaproject.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SessionManager sessionManager;
    private CartManager cartManager;
    private TextView tvCartBadge;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in first
        sessionManager = new SessionManager(this);
        Log.d(TAG, "isLoggedIn: " + sessionManager.isLoggedIn());
        Log.d(TAG, "Token: " + sessionManager.getToken());
        
        if (!sessionManager.isLoggedIn()) {
            // Redirect to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // testApiConnection(); // Uncomment this line to test API connection

        initViews();
        setupHeader();
        setupBottomNavigation();
        setupCategoryCards();
        setupFeaturedProducts();
        updateCartBadge();
        
        // Load the home fragment by default
        loadFragment(new HomeFragment());
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
    }

    private void handleSearchClick() {
        loadFragment(new SearchFragment());
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
    }

    private void handleNotificationsClick() {
        Toast.makeText(this, "Notifications will be implemented", Toast.LENGTH_SHORT).show();
        // TODO: Implement notifications functionality
    }

    private void handleCartClick() {
        loadFragment(new CartFragment());
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
    }

    private void handleProfileClick() {
        loadFragment(new com.example.brewbuyjavaproject.ui.profile.ProfileFragment());
        bottomNavigationView.setSelectedItemId(R.id.ivProfile);
    }

    private void handleViewAllProductsClick() {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
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
                Fragment fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.nav_search) {
                    fragment = new SearchFragment();
                } else if (itemId == R.id.nav_cart) {
                    fragment = new CartFragment();
                } else if (itemId == R.id.nav_order) {
                    fragment = new OrderFragment();
                }
                
                if (fragment != null) {
                    loadFragment(fragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void setupCategoryCards() {
        // This method is now handled in HomeFragment
    }

    private void setupFeaturedProducts() {
        // This method is now handled in HomeFragment
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

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
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