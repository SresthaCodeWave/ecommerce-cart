package com.example.cart.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "localhost:8081/merchant",name="cart")
public interface FeignInterface {
    @GetMapping("getPrice/{merchantId}/{productId}")
    public Integer getPrice(@PathVariable("merchantId") String merchantId,@PathVariable("productId") String productId);
    @GetMapping("getstocks/{merchantId}/{productId}")
    public  Integer getStocks(@PathVariable("merchantId") String merchantId,@PathVariable("productId") String productId);
    @PutMapping("/updateStocks/{merchantId}/{productId}/{quantity}")
    public Boolean updateStocks(@PathVariable("merchantId") String merchantEmailId,@PathVariable("productId") String productId,@PathVariable("quantity") Integer quantity);
    @GetMapping("getMerchantName/{merchantId}")
    public String getMerchantName(@PathVariable("merchantId") String merchantId);

    }
