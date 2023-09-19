package com.example.cart.service.impl;

import com.example.cart.dto.OrderDTO;
import com.example.cart.dto.ShowCartDTO;
import com.example.cart.entity.Cart;
import com.example.cart.entity.CartItems;
import com.example.cart.entity.Order;
import com.example.cart.repository.CartRepository;
import com.example.cart.repository.OrderRepository;
import com.example.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    OrderRepository orderRepository;

    public void createCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCartItem(Collections.emptyList());
        cartRepository.save(cart);
    }
    public void createOrder(String userId){
        Order order=new Order();
        order.setUserId(userId);
        order.setCartItem(Collections.emptyList());
        orderRepository.save(order);
    }
    public void addToCart(String cartId, String productId, String merchantId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        Cart cart = optionalCart.get();
        boolean b = true;
        if (optionalCart.isPresent()) {
            List<CartItems> cartItemsList = cart.getCartItem();
            for (CartItems cartItems : cartItemsList) {
                if (cartItems.getProductId().equals(productId) && (cartItems.getMerchantId()).equals(merchantId)) {
                    b = false;
                    cartItems.setProductQuantity(cartItems.getProductQuantity() + 1);
                    cart.setCartItem(cartItemsList);
                    cartRepository.save(cart);
                }
            }
            if (b) {
                CartItems cartItems = new CartItems();
                cartItems.setMerchantId(merchantId);
                cartItems.setProductId(productId);
                cartItems.setProductQuantity(1);
                cartItemsList.add(cartItems);
                cart.setCartItem(cartItemsList);
                cartRepository.save(cart);

            }
        }

    }

    public void deleteOneCartItem(String cartId, String productId, String merchantId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        Cart cart = optionalCart.get();
        if (optionalCart.isPresent()) {
            List<CartItems> cartItemsList = cart.getCartItem();
            for (CartItems cartItems : cartItemsList) {
                if (cartItems.getProductId().equals(productId) && (cartItems.getMerchantId()).equals(merchantId)) {
                    if (cartItems.getProductQuantity() == 1){
                        cartItemsList.remove(cartItems);
                        cart.setCartItem(cartItemsList);
                        cartRepository.save(cart);
                        break;
                    }
                    else {
                        cartItems.setProductQuantity(cartItems.getProductQuantity() - 1);
                        cart.setCartItem(cartItemsList);
                        cartRepository.save(cart);
                    }
                }
            }
        }
    }
    public Optional<Cart> find(String cartId) {
          return  cartRepository.findById(cartId);

          }
    public Optional<Order> findById(String cartId) {
        return orderRepository.findById(cartId);
    }
    public void updateOrder(Order order){
        orderRepository.save(order);
    }
    public void updateCart(Cart cart){
        cartRepository.save(cart);
    }
    }




