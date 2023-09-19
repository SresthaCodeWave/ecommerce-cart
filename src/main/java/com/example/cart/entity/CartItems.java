package com.example.cart.entity;

import lombok.Data;

@Data
public class CartItems {
    private String merchantId;
    private String productId;
    private Integer productQuantity;
}
