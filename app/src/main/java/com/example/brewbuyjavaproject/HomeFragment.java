package com.example.brewbuyjavaproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brewbuyjavaproject.Model.Product;
import com.example.brewbuyjavaproject.network.ProductDataManager;
import com.example.brewbuyjavaproject.ui.main.FeaturedProductAdapter;
import com.example.brewbuyjavaproject.ui.products.ProductListActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductDataManager.ProductDataCallback, FeaturedProductAdapter.OnProductClickListener {
    private static final String TAG = "HomeFragment";
    private RecyclerView rvFeaturedProducts;
    private FeaturedProductAdapter featuredProductAdapter;
    private List<Product> featuredProducts;
    private ProductDataManager productDataManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupCategoryCards(view);
        setupFeaturedProducts(view);
        setupViewAllProducts(view);

        // Load featured products
        productDataManager = new ProductDataManager(requireContext());
        loadFeaturedProducts();

        return view;
    }

    private void initViews(View view) {
        rvFeaturedProducts = view.findViewById(R.id.rvFeaturedProducts);
    }

    private void setupCategoryCards(View view) {
        MaterialCardView cardCoffeeBeans = view.findViewById(R.id.cardCoffeeBeans);
        MaterialCardView cardCoffeeMachines = view.findViewById(R.id.cardCoffeeMachines);
        MaterialCardView cardAccessories = view.findViewById(R.id.cardAccessories);
        MaterialCardView cardMerchandise = view.findViewById(R.id.cardMerchandise);

        // Add null checks to prevent crashes
        if (cardCoffeeBeans != null) {
            cardCoffeeBeans.setOnClickListener(v -> 
                Toast.makeText(requireContext(), "Coffee Beans clicked", Toast.LENGTH_SHORT).show()
            );
        }

        if (cardCoffeeMachines != null) {
            cardCoffeeMachines.setOnClickListener(v -> 
                Toast.makeText(requireContext(), "Coffee Machines clicked", Toast.LENGTH_SHORT).show()
            );
        }

        if (cardAccessories != null) {
            cardAccessories.setOnClickListener(v -> 
                Toast.makeText(requireContext(), "Accessories clicked", Toast.LENGTH_SHORT).show()
            );
        }

        if (cardMerchandise != null) {
            cardMerchandise.setOnClickListener(v -> 
                Toast.makeText(requireContext(), "Merchandise clicked", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void setupFeaturedProducts(View view) {
        rvFeaturedProducts = view.findViewById(R.id.rvFeaturedProducts);
        if (rvFeaturedProducts != null) {
            featuredProducts = new ArrayList<>();
            featuredProductAdapter = new FeaturedProductAdapter(featuredProducts, this);
            rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            rvFeaturedProducts.setAdapter(featuredProductAdapter);
        }
    }

    private void loadFeaturedProducts() {
        if (productDataManager != null) {
            productDataManager.loadProducts(this);
        }
    }

    private void setupViewAllProducts(View view) {
        TextView tvViewAllProducts = view.findViewById(R.id.tvViewAllProducts);
        if (tvViewAllProducts != null) {
            tvViewAllProducts.setOnClickListener(v -> {
                // Navigate to product list activity
                startActivity(new Intent(requireContext(), ProductListActivity.class));
            });
        }
    }

    @Override
    public void onSuccess(List<Product> products) {
        Log.d(TAG, "Loaded " + products.size() + " products");
        // For featured products, we'll just show the first 5 products
        if (featuredProducts != null && featuredProductAdapter != null) {
            int count = Math.min(products.size(), 5);
            featuredProducts.clear();
            featuredProducts.addAll(products.subList(0, count));
            featuredProductAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "Error loading products: " + error);
        Toast.makeText(requireContext(), "Error loading products: " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProductClick(Product product) {
        // Handle product click - for now just show a toast
        if (requireContext() != null) {
            Toast.makeText(requireContext(), "Product clicked: " + product.getName(), Toast.LENGTH_SHORT).show();
        }
    }
}