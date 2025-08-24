// app/src/main/java/com/example/brewbuyjavaproject/network/AuthInterceptor.java
package com.example.brewbuyjavaproject.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.brewbuyjavaproject.utils.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static final String TAG = "AuthInterceptor";
    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        SessionManager sessionManager = new SessionManager(context);
        
        // Get token from session manager
        String token = sessionManager.getToken();

        Log.d(TAG, "Token from session: " + token);
        Log.d(TAG, "Request URL: " + originalRequest.url());
        Log.d(TAG, "Request method: " + originalRequest.method());
        
        // Log request headers
        for (String name : originalRequest.headers().names()) {
            Log.d(TAG, "Request Header: " + name + " = " + originalRequest.headers().get(name));
        }
        
        // If token exists, add it to the request
        if (token != null && !token.isEmpty()) {
            Log.d(TAG, "Adding Authorization header with token");
            Request.Builder builder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token);
            Request newRequest = builder.build();
            Log.d(TAG, "Proceeding with authenticated request to: " + newRequest.url());
            return chain.proceed(newRequest);
        }
        
        Log.d(TAG, "No token found, proceeding without authentication for URL: " + originalRequest.url());
        // If no token, proceed with original request
        return chain.proceed(originalRequest);
    }
}