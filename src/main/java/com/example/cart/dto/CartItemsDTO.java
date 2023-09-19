package com.example.cart.dto;

import lombok.Data;

@Data
public class CartItemsDTO {
    private String merchantId;
    private String productId;
    private Integer productQuantity;
}
