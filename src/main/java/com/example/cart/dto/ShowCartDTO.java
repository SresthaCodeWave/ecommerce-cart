package com.example.cart.dto;

import com.example.cart.entity.Product;
import lombok.Data;

@Data
public class ShowCartDTO {
    private String merchantId;
    private String merchantName;
    private Product product;
    private Integer price;
    private Integer quantity;

}
