package com.example.cart.feign;

import com.example.cart.dto.ProductDTO;
import com.example.cart.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "localhost:8085/products",name = "product")
public interface FeignProductInterface {
    @GetMapping("/findById/{productId}")
    public ProductDTO findById(@PathVariable("productId") String productId);

}
