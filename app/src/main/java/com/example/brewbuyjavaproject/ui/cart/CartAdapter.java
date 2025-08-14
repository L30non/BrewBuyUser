// app/src/main/java/com/example/brewbuyjavaproject/ui/cart/CartAdapter.java
package com.example.brewbuyjavaproject.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brewbuyjavaproject.Model.CartItem;
import com.example.brewbuyjavaproject.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
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

            // Load image
            if (item.getProduct().getImageUrl() != null && !item.getProduct().getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(item.getProduct().getImageUrl())
                        .placeholder(R.drawable.ic_coffee)
                        .error(R.drawable.ic_coffee)
                        .into(ivProductImage);
            } else {
                ivProductImage.setImageResource(R.drawable.ic_coffee);
            }

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
    }
}
