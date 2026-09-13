package com.example.kafkaorders.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fake-payment")
public class FakePaymentGatewayController {

    @PostMapping("/charge")
    public String charge() {
        throw new RuntimeException("PAYMENT GATEWAY DOWN");
    }
}