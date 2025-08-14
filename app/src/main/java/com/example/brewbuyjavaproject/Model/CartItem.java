// app/src/main/java/com/example/brewbuyjavaproject/Model/CartItem.java
package com.example.brewbuyjavaproject.Model;

import java.math.BigDecimal;

public class CartItem {
    private Product product;
    private int quantity;
    private BigDecimal totalPrice;

    public CartItem() {}

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and Setters
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotalPrice();
    }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public void updateTotalPrice() {
        if (product != null && quantity > 0) {
            this.totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }

    public void incrementQuantity() {
        this.quantity++;
        updateTotalPrice();
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
            updateTotalPrice();
        }
    }
}