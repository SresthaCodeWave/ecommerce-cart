package com.example.cart.controller;

import com.example.cart.dto.*;
import com.example.cart.entity.Cart;
import com.example.cart.entity.CartItems;
import com.example.cart.entity.Order;
import com.example.cart.entity.Product;
import com.example.cart.feign.FeignInterface;
import com.example.cart.feign.FeignMailInterface;
import com.example.cart.feign.FeignProductInterface;
import com.example.cart.service.CartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    FeignProductInterface feignProductInterface;
     @Autowired
    FeignInterface feignInterface;
     @Autowired
    FeignMailInterface feignMailInterface;

    @PostMapping("/createCart")
    public ResponseEntity<Boolean> createCart(@RequestBody UserDTO userDTO) {
        cartService.createCart(userDTO.getEmailId());
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
 @PostMapping("/createOrder")
 public ResponseEntity<Boolean> createOrder(@RequestBody UserDTO userDTO) {
     cartService.createOrder(userDTO.getEmailId());
     return new ResponseEntity<Boolean>(true, HttpStatus.OK);
 }
    @PutMapping("/addItemToCart/{cartId}/{merchantId}/{productId}")
    public ResponseEntity<Boolean> addItemToCart(@PathVariable("cartId") String cartId, @PathVariable("merchantId") String merchantId, @PathVariable("productId") String productId) {
        cartService.addToCart(cartId, productId, merchantId);
        return new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK);
    }
//    @PutMapping("/directlyBuyNow")
//    public ResponseEntity<Boolean> directlyBuy(@RequestParam String productId,@RequestParam String merchantId,@RequestParam Integer quantity){
//      if(quantity>feignInterface.getStocks(merchantId,productId)) {
//         return ResponseEntity<Boolean>(Boolean.FALSE,HttpStatus.OK) ;
//      }
//      else {
//          ShowCartDTO showCartDTO = new ShowCartDTO();
//          Product product = new Product();
//          ProductDTO productDTO = feignProductInterface.findById(productId);
//          BeanUtils.copyProperties(productDTO, product);
//          showCartDTO.setProduct(product);
//          showCartDTO.setQuantity(quantity);
//          showCartDTO.setMerchantName(feignInterface.getMerchantName(merchantId));
//          showCartDTO.setMerchantId(merchantId);
//          showCartDTO.setPrice(feignInterface.getPrice(merchantId,productId));
//
//      }
//    }
    @DeleteMapping("/deleteOneCartItem/{cartId}/{productId}/{merchantId}")
    public ResponseEntity<Boolean> deleteOneCartItem(@PathVariable("cartId") String cartId, @PathVariable("merchantId") String merchantId, @PathVariable("productId") String productId) {
        cartService.deleteOneCartItem(cartId, productId, merchantId);
        return new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK);
    }


    @GetMapping("/showCart/{cartId}")
    public ResponseEntity<List<ShowCartDTO>> findAllCartItems(@PathVariable("cartId") String cartId) {
        Optional<Cart> OptionalCart = cartService.find(cartId);
        Cart cart = OptionalCart.get();
        List<CartItems> cartItems = cart.getCartItem();
        List<ShowCartDTO> showCartDTOList = new ArrayList<ShowCartDTO>();
        for (CartItems cartItems1 : cartItems) {
            ShowCartDTO showCartDTO = new ShowCartDTO();
            ProductDTO productDTO = feignProductInterface.findById(cartItems1.getProductId());
            Product product=new Product();
            BeanUtils.copyProperties(productDTO,product);
            showCartDTO.setProduct(product);
            showCartDTO.setMerchantName(feignInterface.getMerchantName(cartItems1.getMerchantId()));
            showCartDTO.setPrice(feignInterface.getPrice(cartItems1.getMerchantId(),cartItems1.getProductId()));
            showCartDTO.setMerchantId(cartItems1.getMerchantId());
            showCartDTO.setQuantity(cartItems1.getProductQuantity());
            showCartDTOList.add(showCartDTO);
        }
        return new ResponseEntity<List<ShowCartDTO>>(showCartDTOList, HttpStatus.OK);
    }
    @PutMapping("/orderNow/{cartId}")
    public ResponseEntity<String> orderNow(@PathVariable("cartId") String cartId) {
        Optional<Cart> optionalCart = cartService.find(cartId);
        Cart cart = optionalCart.get();
        Optional<Order> optionalOrder = cartService.findById(cartId);
        Order order = optionalOrder.get();
        List<CartItems> cartItemsList = cart.getCartItem();
        List<ShowCartDTO> showCartDTOList=order.getCartItem();
        boolean b=true;
        for (CartItems cartItems1 : cartItemsList) {
            if((feignInterface.getStocks(cartItems1.getMerchantId(),cartItems1.getProductId()))<cartItems1.getProductQuantity()) {
              b=false;
              break;
            } }
         if(b){
        for (CartItems cartItems1 : cartItemsList) {
                ProductDTO productDTO = feignProductInterface.findById(cartItems1.getProductId());
                ShowCartDTO showCartDTO = new ShowCartDTO();
                Product product = new Product();
                BeanUtils.copyProperties(productDTO, product);
                showCartDTO.setProduct(product);
                showCartDTO.setPrice(feignInterface.getPrice(cartItems1.getMerchantId(), cartItems1.getProductId()));
                showCartDTO.setMerchantId(cartItems1.getMerchantId());
                showCartDTO.setMerchantName(feignInterface.getMerchantName(cartItems1.getMerchantId()));
                showCartDTO.setQuantity(cartItems1.getProductQuantity());
                showCartDTOList.add(showCartDTO);
                feignInterface.updateStocks(cartItems1.getMerchantId(), cartItems1.getProductId(),(-1*(cartItems1.getProductQuantity())));
            }
            order.setCartItem(showCartDTOList);
            cartService.updateOrder(order);
            cart.setCartItem(Collections.emptyList());
            cartService.updateCart(cart);
            MailDTO email= new MailDTO();
            email.setRecipientMail(cartId);
            String message=feignMailInterface.sendMail(email);
            return new ResponseEntity<String>("approved", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>("not available stock",HttpStatus.OK);
        }

    }


    @GetMapping("/showOrderHistory/{userId}")
    public ResponseEntity<List<ShowCartDTO>> showOrderHistory(@PathVariable("userId") String userId){
        Optional<Order> optionalOrder=cartService.findById(userId);
        Order order=optionalOrder.get();
        return new ResponseEntity<List<ShowCartDTO>>(order.getCartItem(),HttpStatus.OK);
        }
}
