// app/src/main/java/com/example/brewbuyjavaproject/ui/auth/LoginActivity.java
package com.example.brewbuyjavaproject.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.brewbuyjavaproject.MainActivity;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.Model.User;
import com.example.brewbuyjavaproject.network.ApiService;
import com.example.brewbuyjavaproject.network.RetrofitClient;
import com.example.brewbuyjavaproject.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView tvRegister;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        sessionManager = new SessionManager(this);

        btnLogin.setOnClickListener(v -> loginUser());
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        showLoading(true);

        // Create a simple user object for login
        User loginRequest = new User();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        Log.d(TAG, "Attempting login with username: " + username);

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<User> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showLoading(false);
                Log.d(TAG, "Login response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    User loggedInUser = response.body();
                    Log.d(TAG, "Login successful for user: " + loggedInUser.getUsername());
                    Log.d(TAG, "User object: " + loggedInUser.toString());

                    // Extract token from the User object
                    String token = loggedInUser.getToken();
                    if (token != null && !token.isEmpty()) {
                        Log.d(TAG, "Token extracted from User object: " + token);
                    } else {
                        Log.e(TAG, "Token not found in User object");
                    }

                    sessionManager.createLoginSession(
                            loggedInUser.getId() != null ? loggedInUser.getId().intValue() : 0,
                            loggedInUser.getUsername(),
                            loggedInUser.getEmail(),
                            token // Use token from User object
                    );

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e(TAG, "Login failed. Response code: " + response.code() + ", Error: " + errorBody);
                        Toast.makeText(LoginActivity.this, "Login failed: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "Login failed", t);
                Toast.makeText(LoginActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
        }
    }
}