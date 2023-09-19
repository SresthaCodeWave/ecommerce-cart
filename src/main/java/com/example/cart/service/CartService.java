package com.example.cart.service;

import com.example.cart.entity.Cart;
import com.example.cart.entity.CartItems;
import com.example.cart.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface CartService {
    public void createCart(String userId);
    public void addToCart(String cartId,String productId,String merchantId);
    public void deleteOneCartItem(String cartId, String productId, String merchantId);
    public Optional<Cart> find(String cartId);
    public void createOrder(String userId);
    public Optional<Order> findById(String cartId);
    public void updateOrder(Order order);
    public void updateCart(Cart cart);

    }
