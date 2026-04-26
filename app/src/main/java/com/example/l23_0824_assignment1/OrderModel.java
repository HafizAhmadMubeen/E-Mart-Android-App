package com.example.l23_0824_assignment1;

public class OrderModel {
    private String orderId, orderDate, orderStatus, totalAmount, productDetails, buyerId, sellerId;

    public OrderModel() {}

    public OrderModel(String orderId, String orderDate, String orderStatus,
                      String totalAmount, String productDetails, String buyerId, String sellerId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.productDetails = productDetails;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }

    public String getOrderId() { return orderId; }
    public String getOrderDate() { return orderDate; }
    public String getOrderStatus() { return orderStatus; }
    public String getTotalAmount() { return totalAmount; }
    public String getProductDetails() { return productDetails; }
    public String getBuyerId() { return buyerId; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
}