// app/src/main/java/com/example/brewbuyjavaproject/network/OrderApiService.java
package com.example.brewbuyjavaproject.network;

import com.example.brewbuyjavaproject.Model.Order;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderApiService {
    // Order Management
    @POST("api/orders")
    Call<Order> createOrder(@Body Order order);

    @GET("api/orders/{id}")
    Call<Order> getOrderById(@Path("id") Long id);

    @GET("api/orders/user/me")
    Call<List<Order>> getMyOrders();

    @PUT("api/orders/{id}/status")
    Call<Order> updateOrderStatus(@Path("id") Long id, @Query("status") String status);

    @DELETE("api/orders/{id}")
    Call<Void> deleteOrder(@Path("id") Long id);
}