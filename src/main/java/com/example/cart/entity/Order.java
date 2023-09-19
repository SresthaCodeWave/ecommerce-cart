package com.example.cart.entity;

import com.example.cart.dto.ShowCartDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document
public class Order {
    @Id
    private String userId;
    private List<ShowCartDTO> cartItem;
}
