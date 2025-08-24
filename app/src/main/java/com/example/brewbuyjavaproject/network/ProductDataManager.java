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
                    List<Product> products = response.body();
                    Log.d(TAG, "Products loaded successfully: " + products.size());
                    
                    // Log details about the first few products for debugging
                    for (int i = 0; i < Math.min(3, products.size()); i++) {
                        Product product = products.get(i);
                        Log.d(TAG, "Product " + i + ": " + product.getName() + 
                              ", ID: " + product.getId() + 
                              ", ImageBase64: " + (product.getImageBase64() != null ? product.getImageBase64().length() + " chars" : "null") +
                              ", ImageType: " + product.getImageType());
                    }
                    
                    callback.onSuccess(products);
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