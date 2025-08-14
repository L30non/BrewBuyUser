// app/src/main/java/com/example/brewbuyjavaproject/network/ApiService.java
package com.example.brewbuyjavaproject.network;

import com.example.brewbuyjavaproject.Model.Product;
import com.example.brewbuyjavaproject.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Authentication
    @POST("api/auth/register")
    Call<User> registerUser(@Body User user);

    @POST("api/auth/login")
    Call<User> loginUser(@Body User user);

    // Test endpoint
    @GET("api/auth/test")
    Call<String> testConnection();

    // *** CHANGED: Ensure proper annotations for products ***
    @GET("api/products")
    Call<List<Product>> getAllProducts();

    @GET("api/products/{id}")
    Call<Product> getProductById(@Path("id") Long id);
}