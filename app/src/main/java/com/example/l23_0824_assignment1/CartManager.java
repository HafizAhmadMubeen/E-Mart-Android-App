package com.example.l23_0824_assignment1;

import java.util.ArrayList;

public class CartManager {
    public static ArrayList<CartItem> cartList = new ArrayList<>();

    public static void addProduct(Product product) {
        // Check if this product is already in the cart
        for (CartItem item : cartList) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        cartList.add(new CartItem(product, 1));
    }
}
