package com.example.cart.dto;

import lombok.Data;

@Data
public class MailDTO {

        private String recipientMail;
        private String msgBody="Thank You For Your Order!🥳🥳   \nWe will be Waiting For Your Next Order😌😇";
        private String subject="Order Details";


    }

