package com.example.l23_0824_assignment1;

public class Product {

    private String productId;
    private String productName;
    private String productPrice;
    private String productDescription;
    private String productType;
    private String sellerId;

    private int imageRes;
    private boolean isFavourite;

    // Required for Firebase
    public Product() { }

    public Product(String productId, String productName, String productPrice,
                   String productDescription, String productType, String sellerId) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productType = productType;
        this.sellerId = sellerId;
    }


    public String getProductName() { return productName; }
    public String getProductPrice() { return productPrice; }
    public String getProductDescription() { return productDescription; }


    public String getId() { return productId; }
    public String getName() { return (productName != null) ? productName : "Unknown Product"; }
    public String getPrice() { return (productPrice != null) ? productPrice : "0.00"; }
    public String getDescription() { return (productDescription != null) ? productDescription : "No description available"; }
    public String getProductType() { return productType; }
    public int getImageRes() { return imageRes == 0 ? R.drawable.onboarding_icon : imageRes; }
    public boolean isFavourite() { return isFavourite; }

    // Setters — unchanged
    public void setProductId(String productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setProductPrice(String productPrice) { this.productPrice = productPrice; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    public void setProductType(String productType) { this.productType = productType; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public void setImageRes(int imageRes) { this.imageRes = imageRes; }
}