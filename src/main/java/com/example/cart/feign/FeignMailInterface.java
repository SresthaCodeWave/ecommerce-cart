package com.example.cart.feign;

import com.example.cart.dto.MailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "localhost:9008/mail",value = "mail")
public interface FeignMailInterface {
    @PostMapping("/sendMail")
    public String sendMail(MailDTO details);
}
