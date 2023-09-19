package com.example.cart.dto;

import lombok.Data;

import java.util.List;
@Data
public class OrderDTO {
    private List<ShowCartDTO> cartItem;
}
