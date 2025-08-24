package com.example.brewbuyjavaproject.ui.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.brewbuyjavaproject.Model.Product;
import com.example.brewbuyjavaproject.R;
import com.example.brewbuyjavaproject.utils.ImageUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.FeaturedProductViewHolder> {
    private static final String TAG = "FeaturedProductAdapter";
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public FeaturedProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeaturedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured_product, parent, false);
        return new FeaturedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class FeaturedProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName, tvProductPrice;

        public FeaturedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }

        public void bind(Product product) {
            tvProductName.setText(product.getName());
            tvProductPrice.setText("$" + product.getPrice());

            // Load image using Base64 data
            loadProductImage(product);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }

        private void loadProductImage(Product product) {
            String base64Image = product.getImageBase64();
            
            if (base64Image != null && !base64Image.isEmpty()) {
                Log.d(TAG, "Featured product " + product.getId() + " has Base64 image data, length: " + base64Image.length());
                
                // Convert Base64 to stream for Glide
                ByteArrayInputStream imageStream = ImageUtils.base64ToStream(base64Image);
                
                if (imageStream != null) {
                    Log.d(TAG, "Successfully converted Base64 to stream for featured product " + product.getId());
                    Glide.with(itemView.getContext())
                            .load(imageStream)
                            .diskCacheStrategy(DiskCacheStrategy.NONE) // Disable disk cache for streams
                            .skipMemoryCache(true) // Skip memory cache to avoid issues with streams
                            .placeholder(R.drawable.ic_coffee)
                            .error(R.drawable.ic_coffee)
                            .listener(new RequestListener<android.graphics.drawable.Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                                    Log.e(TAG, "Glide failed to load image for featured product " + product.getId(), e);
                                    return false; // Allow error drawable to be shown
                                }

                                @Override
                                public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, Target<android.graphics.drawable.Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    Log.d(TAG, "Glide successfully loaded image for featured product " + product.getId());
                                    return false; // Allow drawable to be set
                                }
                            })
                            .into(ivProductImage);
                } else {
                    Log.e(TAG, "Failed to convert Base64 to stream for featured product " + product.getId());
                    // Fallback to placeholder if conversion fails
                    ivProductImage.setImageResource(R.drawable.ic_coffee);
                }
            } else {
                Log.d(TAG, "Featured product " + product.getId() + " has no Base64 image data");
                // Use placeholder if no image data
                ivProductImage.setImageResource(R.drawable.ic_coffee);
            }
        }
    }
}