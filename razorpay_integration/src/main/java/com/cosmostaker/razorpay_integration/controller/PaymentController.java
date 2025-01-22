package com.cosmostaker.razorpay_integration.controller;

import com.cosmostaker.razorpay_integration.entity.OrderDetails;
import com.cosmostaker.razorpay_integration.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<OrderDetails> createOrder(@RequestParam("productId") Long productId) {
        try {
            OrderDetails order = paymentService.createOrder(productId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/updatePaymentDetails")
    public ResponseEntity<String> updatePaymentDetails(@RequestParam String order_id,
                                                       @RequestParam String payment_id,
                                                       @RequestParam String signature) {
        boolean isUpdated = paymentService.updatePaymentDetails(order_id, payment_id, signature);

        if (isUpdated) {
            return ResponseEntity.ok("Payment details updated successfully.");
        } else {
            return ResponseEntity.status(400).body("Failed to update payment details.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestParam String orderId,
                                        @RequestParam String paymentId,
                                        @RequestParam String razorpaySignature) {
        try {
            boolean isValid = paymentService.verifyPayment(orderId, paymentId, razorpaySignature);
            if (isValid) {
                return ResponseEntity.ok("Payment verified successfully");
            } else {
                return ResponseEntity.status(400).body("Payment verification failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error verifying payment");
        }
    }

}