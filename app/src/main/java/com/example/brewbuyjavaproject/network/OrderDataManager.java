package com.example.brewbuyjavaproject.network;

import android.content.Context;
import android.util.Log;

import com.example.brewbuyjavaproject.Model.Order;
import com.example.brewbuyjavaproject.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderDataManager {
    private static final String TAG = "OrderDataManager";
    private Context context;
    
    public OrderDataManager(Context context) {
        this.context = context;
    }
    
    public void createOrder(Order order, OrderCallback callback) {
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getToken();
        
        Log.d(TAG, "Token from session manager in createOrder: " + token);
        
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "User not authenticated - token is null or empty");
            callback.onError("User not authenticated");
            return;
        }
        
        Log.d(TAG, "Creating order with token: Present");
        Log.d(TAG, "Order object: " + order.toString());
        
        OrderApiService orderApiService = RetrofitClient.getOrderApiService(context);
        Call<Order> call = orderApiService.createOrder(order);
        
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, retrofit2.Response<Order> response) {
                Log.d(TAG, "Create order API response code: " + response.code());
                Log.d(TAG, "Response message: " + response.message());
                
                // Log response headers
                for (String name : response.headers().names()) {
                    Log.d(TAG, "Response Header: " + name + " = " + response.headers().get(name));
                }
                
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Order created successfully: " + response.body().getId());
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Failed to create order. Response code: " + response.code() + ", Error: " + errorBody);
                        callback.onError("Failed to create order. Code: " + response.code() + ", Error: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                        callback.onError("Failed to create order. Code: " + response.code());
                    }
                }
            }
            
            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e(TAG, "Network error when creating order", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    public void getMyOrders(OrderListCallback callback) {
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getToken();
        
        Log.d(TAG, "Token from session manager in getMyOrders: " + token);
        
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "User not authenticated - token is null or empty");
            callback.onError("User not authenticated");
            return;
        }
        
        Log.d(TAG, "Getting my orders with token: Present");
        
        OrderApiService orderApiService = RetrofitClient.getOrderApiService(context);
        Call<List<Order>> call = orderApiService.getMyOrders();
        
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, retrofit2.Response<List<Order>> response) {
                Log.d(TAG, "Get my orders API response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Orders loaded successfully: " + response.body().size());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Failed to load orders. Response code: " + response.code());
                    callback.onError("Failed to load orders. Code: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e(TAG, "Network error when loading orders", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    public void getOrderById(Long orderId, OrderCallback callback) {
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getToken();
        
        Log.d(TAG, "Token from session manager in getOrderById: " + token);
        
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "User not authenticated - token is null or empty");
            callback.onError("User not authenticated");
            return;
        }
        
        Log.d(TAG, "Getting order by ID with token: Present");
        
        OrderApiService orderApiService = RetrofitClient.getOrderApiService(context);
        Call<Order> call = orderApiService.getOrderById(orderId);
        
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, retrofit2.Response<Order> response) {
                Log.d(TAG, "Get order by ID API response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Order loaded successfully: " + response.body().getId());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Failed to load order. Response code: " + response.code());
                    callback.onError("Failed to load order. Code: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e(TAG, "Network error when loading order", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    public void updateOrderStatus(Long orderId, String status, OrderCallback callback) {
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getToken();
        
        Log.d(TAG, "Token from session manager in updateOrderStatus: " + token);
        
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "User not authenticated - token is null or empty");
            callback.onError("User not authenticated");
            return;
        }
        
        Log.d(TAG, "Updating order status with token: Present");
        
        OrderApiService orderApiService = RetrofitClient.getOrderApiService(context);
        Call<Order> call = orderApiService.updateOrderStatus(orderId, status);
        
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, retrofit2.Response<Order> response) {
                Log.d(TAG, "Update order status API response code: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Order status updated successfully: " + response.body().getId());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "Failed to update order status. Response code: " + response.code());
                    callback.onError("Failed to update order status. Code: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e(TAG, "Network error when updating order status", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    public void deleteOrder(Long orderId, DeleteOrderCallback callback) {
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getToken();
        
        Log.d(TAG, "Token from session manager in deleteOrder: " + token);
        
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "User not authenticated - token is null or empty");
            callback.onError("User not authenticated");
            return;
        }
        
        Log.d(TAG, "Deleting order with token: Present");
        
        OrderApiService orderApiService = RetrofitClient.getOrderApiService(context);
        Call<Void> call = orderApiService.deleteOrder(orderId);
        
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Log.d(TAG, "Delete order API response code: " + response.code());
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "Order deleted successfully");
                    callback.onSuccess();
                } else {
                    Log.e(TAG, "Failed to delete order. Response code: " + response.code());
                    callback.onError("Failed to delete order. Code: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Network error when deleting order", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    
    public interface OrderCallback {
        void onSuccess(Order order);
        void onError(String error);
    }
    
    public interface OrderListCallback {
        void onSuccess(List<Order> orders);
        void onError(String error);
    }
    
    public interface DeleteOrderCallback {
        void onSuccess();
        void onError(String error);
    }
}