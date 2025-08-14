package com.example.brewbuyjavaproject.utils;

import android.content.Context;
import com.example.brewbuyjavaproject.Model.CartItem;
import com.example.brewbuyjavaproject.Model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String CART_PREFS = "CartPrefs";
    private static final String CART_ITEMS_KEY = "CartItems";
    private Context context;
    private List<CartItem> cartItems;
    private OnCartUpdateListener listener;

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    public CartManager(Context context) {
        this.context = context;
        loadCartFromPreferences();
    }

    private void loadCartFromPreferences() {
        android.content.SharedPreferences prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
        String cartJson = prefs.getString(CART_ITEMS_KEY, null);

        if (cartJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartItem>>(){}.getType();
            cartItems = gson.fromJson(cartJson, type);
        } else {
            cartItems = new ArrayList<>();
        }
    }

    private void saveCartToPreferences() {
        android.content.SharedPreferences prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String cartJson = gson.toJson(cartItems);
        editor.putString(CART_ITEMS_KEY, cartJson);
        editor.apply();
    }

    public void addToCart(Product product) {
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.incrementQuantity();
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem newItem = new CartItem(product, 1);
            cartItems.add(newItem);
        }

        saveCartToPreferences();
        notifyCartUpdated();
    }

    public void removeFromCart(long productId) {
        cartItems.removeIf(item -> item.getProduct().getId() == productId);
        saveCartToPreferences();
        notifyCartUpdated();
    }

    public void updateQuantity(long productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                item.setQuantity(quantity);
                item.updateTotalPrice();
                break;
            }
        }
        saveCartToPreferences();
        notifyCartUpdated();
    }

    public void incrementQuantity(long productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                item.incrementQuantity();
                break;
            }
        }
        saveCartToPreferences();
        notifyCartUpdated();
    }

    public void decrementQuantity(long productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                item.decrementQuantity();
                if (item.getQuantity() <= 0) {
                    removeFromCart(productId);
                } else {
                    saveCartToPreferences();
                    notifyCartUpdated();
                }
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public int getCartItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getTotalPrice());
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
        saveCartToPreferences();
        notifyCartUpdated();
    }

    public void setOnCartUpdateListener(OnCartUpdateListener listener) {
        this.listener = listener;
    }

    private void notifyCartUpdated() {
        if (listener != null) {
            listener.onCartUpdated();
        }
    }
}
