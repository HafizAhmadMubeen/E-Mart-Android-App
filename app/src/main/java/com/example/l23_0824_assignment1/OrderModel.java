package com.example.l23_0824_assignment1;

public class OrderModel {
    private String orderId, orderDate, orderStatus, totalAmount, productDetails, buyerId;

    public OrderModel() {}

    public OrderModel(String orderId, String orderDate, String orderStatus, String totalAmount, String productDetails, String buyerId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.productDetails = productDetails;
        this.buyerId = buyerId;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getOrderDate() { return orderDate; }
    public String getOrderStatus() { return orderStatus; }
    public String getTotalAmount() { return totalAmount; }
    public String getProductDetails() { return productDetails; }
    public String getBuyerId() { return buyerId; }
}