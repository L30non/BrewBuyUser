package com.example.brewbuyjavaproject.network;

import android.content.Context;
import android.util.Log;

import com.example.brewbuyjavaproject.Model.Product;
import com.example.brewbuyjavaproject.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDataManager {
    private static final String TAG = "ProductDataManager";
    private Context context;
    
    public ProductDataManager(Context context) {
        this.context = context;
    }
    
    public void loadProducts(ProductDataCallback callback) {
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getToken();
        
        Log.d(TAG, "Loading products with token: " + (token != null ? "Present" : "Missing"));
        
        ApiService apiService = RetrofitClient.getApiService(context);
        Call<List<Product>> call = apiService.getAllProducts();
        
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.d(TAG, "Product API response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Products loaded successfully: " + response.body().size());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Failed to load products. Response code: " + response.code());
                    callback.onError("Failed to load products. Code: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e(TAG, "Network error when loading products", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    public interface ProductDataCallback {
        void onSuccess(List<Product> products);
        void onError(String error);
    }
}