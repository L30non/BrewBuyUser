// app/src/main/java/com/example/brewbuyjavaproject/Model/Order.java
package com.example.brewbuyjavaproject.Model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
    private Long id;
    private Long userId;
    private List<OrderItem> items; // Changed from CartItem to OrderItem
    private BigDecimal totalAmount;
    private String status;
    private Date createdAt; // Renamed from orderDate to match API
    private Date updatedAt; // Added to match API
    // Removed shippingAddress and paymentMethod as they might be handled separately or in a more detailed view

    // Constructors
    public Order() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}