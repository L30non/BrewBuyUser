// app/src/main/java/com/example/brewbuyjavaproject/Model/OrderItem.java
package com.example.brewbuyjavaproject.Model;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long productId;
    private int quantity;
    private BigDecimal price;

    // Constructors
    public OrderItem() {}

    public OrderItem(Long productId, int quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}