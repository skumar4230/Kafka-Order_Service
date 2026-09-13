package com.example.kafkaorders.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/fake-payment")
public class FakePaymentGatewayController {

    private final Set<String> processedOrders = new HashSet<>();

    @PostMapping("/charge")
    public String charge(@RequestParam String orderId) {

        if (processedOrders.contains(orderId)) {

            System.out.println(
                    "DUPLICATE PAYMENT REQUEST - ALREADY CHARGED: "
                            + orderId
            );

            return "ALREADY CHARGED";
        }

        processedOrders.add(orderId);

        System.out.println(
                "PAYMENT CHARGED SUCCESSFULLY: "
                        + orderId
        );

        throw new RuntimeException(
                "RESPONSE LOST AFTER PAYMENT"
        );
    }
}