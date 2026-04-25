package com.example.l23_0824_assignment1;

public class SellerProductModelclass {

    private String productId;
    private String productName;
    private String productType;
    private String productPrice;
    private String productDescription;
    private String sellerId; // Critical for identifying which seller owns this product

    // 1. Empty Constructor (REQUIRED for Firebase Realtime Database)
    public SellerProductModelclass() {
    }

    // 2. Full Constructor
    public SellerProductModelclass(String productId, String productName, String productType,
                                   String productPrice, String productDescription, String sellerId) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.sellerId = sellerId;
    }

    // 3. Getters and Setters (REQUIRED for Firebase to map the data)
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getProductPrice() { return productPrice; }
    public void setProductPrice(String productPrice) { this.productPrice = productPrice; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
}