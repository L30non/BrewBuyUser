// app/src/main/java/com/example/brewbuyjavaproject/utils/SessionManager.java
package com.example.brewbuyjavaproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "CoffeeShopPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_JWT_TOKEN = "jwtToken";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String username, String email, String token) {
        android.util.Log.d("SessionManager", "Creating login session with token: " + token);
         editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_EMAIL, email);
        if (token != null) {
            editor.putString(KEY_JWT_TOKEN, token);
        }
        editor.commit();
        android.util.Log.d("SessionManager", "Login session created");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, null);
    }



    public String getToken() {
        String token = pref.getString(KEY_JWT_TOKEN, null);
        android.util.Log.d("SessionManager", "Retrieved token from preferences: " + token);
        return token;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}