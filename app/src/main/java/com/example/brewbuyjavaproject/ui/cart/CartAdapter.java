// app/src/main/java/com/example/brewbuyjavaproject/ui/cart/CartAdapter.java
package com.example.brewbuyjavaproject.ui.cart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.brewbuyjavaproject.Model.CartItem;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.utils.ImageUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private static final String TAG = "CartAdapter";
    private List<CartItem> cartItems;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemRemoved(CartItem item);
    }

    public CartAdapter(List<CartItem> cartItems, CartItemListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateItems(List<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName, tvProductPrice, tvTotalPrice;
        private Button btnDecrease, btnIncrease, btnRemove;
        private TextView tvQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }

        public void bind(CartItem item) {
            tvProductName.setText(item.getProduct().getName());
            tvProductPrice.setText("$" + item.getProduct().getPrice());
            tvTotalPrice.setText("$" + String.format("%.2f", item.getTotalPrice()));
            tvQuantity.setText(String.valueOf(item.getQuantity()));

            // Load image using Base64 data
            loadProductImage(item.getProduct());

            btnDecrease.setOnClickListener(v -> {
                int newQuantity = item.getQuantity() - 1;
                if (listener != null) {
                    listener.onQuantityChanged(item, newQuantity);
                }
            });

            btnIncrease.setOnClickListener(v -> {
                int newQuantity = item.getQuantity() + 1;
                if (listener != null) {
                    listener.onQuantityChanged(item, newQuantity);
                }
            });

            btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemRemoved(item);
                }
            });
        }

        private void loadProductImage(com.example.brewbuyjavaproject.Model.Product product) {
            String base64Image = product.getImageBase64();
            
            if (base64Image != null && !base64Image.isEmpty()) {
                Log.d(TAG, "Cart item " + product.getId() + " has Base64 image data, length: " + base64Image.length());
                
                // Convert Base64 to stream for Glide
                ByteArrayInputStream imageStream = ImageUtils.base64ToStream(base64Image);
                
                if (imageStream != null) {
                    Log.d(TAG, "Successfully converted Base64 to stream for cart item " + product.getId());
                    Glide.with(itemView.getContext())
                            .load(imageStream)
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk cache for streams
                            .skipMemoryCache(true) // Skip memory cache to avoid issues with streams
                            .placeholder(R.drawable.ic_coffee)
                            .error(R.drawable.ic_coffee)
                            .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                                    Log.e(TAG, "Glide failed to load image for cart item " + product.getId(), e);
                                    return false; // Allow error drawable to be shown
                                }

                                @Override
                                public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, Target<android.graphics.drawable.Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Log.d(TAG, "Glide successfully loaded image for cart item " + product.getId());
                                    return false; // Allow drawable to be set
                                }
                            })
                            .into(ivProductImage);
                } else {
                    Log.e(TAG, "Failed to convert Base64 to stream for cart item " + product.getId());
                    // Fallback to placeholder if conversion fails
                    ivProductImage.setImageResource(R.drawable.ic_coffee);
                }
            } else {
                Log.d(TAG, "Cart item " + product.getId() + " has no Base64 image data");
                // Use placeholder if no image data
                ivProductImage.setImageResource(R.drawable.ic_coffee);
            }
        }
    }
}
