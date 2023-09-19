package com.example.cart.entity;

import lombok.Data;

@Data
public class Product {
    private String productId;
    private String productName;
    private String brand;
    private String description;
    private String imgSrc;
}
