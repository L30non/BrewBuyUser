// app/src/main/java/com/example/coffeeshop/network/ApiService.java
package com.example.brewbuyjavaproject.network;

import com.example.brewbuyjavaproject.Model.Product;
import com.example.brewbuyjavaproject.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApiService {
    // Authentication
    @POST("api/auth/register")
    Call<User> registerUser(@Body User user);

    @POST("api/auth/login")
    Call<User> loginUser(@Body User user);

    // Products
    @GET("api/products")
    Call<List<Product>> getAllProducts();

    @GET("api/products/search")
    Call<List<Product>> searchProducts(@Query("keyword") String keyword);
}