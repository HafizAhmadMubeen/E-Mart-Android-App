package com.example.l23_0824_assignment1;

public class Product
{
    private String id;
    private String name;
    private String price;
    private String originalPrice;
    private String description;
    private int imageRes;
    private boolean isFavourite;

    public Product(String id, String name, String price, String originalPrice, String description, int imageRes, boolean isFavourite) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.originalPrice = originalPrice;
        this.description = description;
        this.imageRes = imageRes;
        this.isFavourite = isFavourite;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getDescription() {
        return description;
    }

    public int getImageRes() {
        return imageRes;
    }

    public boolean isFavourite() {
        return isFavourite;
    }
}