package com.example.brewbuyjavaproject;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.brewbuyjavaproject.ui.products.ProductListActivity;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupCategoryCards(view);
        setupFeaturedProducts(view);
        setupViewAllProducts(view);

        return view;
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
        RecyclerView rvFeaturedProducts = view.findViewById(R.id.rvFeaturedProducts);
        if (rvFeaturedProducts != null) {
            rvFeaturedProducts.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            // Setup featured products adapter here
            // For now, just create an empty adapter or mock data
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
}