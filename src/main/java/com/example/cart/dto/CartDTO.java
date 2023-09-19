package com.example.cart.dto;

import com.example.cart.entity.CartItems;
import lombok.Data;

import java.util.List;
@Data
public class CartDTO {
    private String userId;
    private List<CartItems> cartItem;
}
