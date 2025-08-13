package com.example.brewbuyjavaproject.Api;

import com.example.brewbuyjavaproject.Model.Product;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ApiService {
    @GET("products")
    Call<List<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") Long id);

    @POST("products")
    Call<Product> createProduct(@Body Product product);

    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") Long id, @Body Product product);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") Long id);
}

