package com.cosmostaker.razorpay_integration.service;

import com.cosmostaker.razorpay_integration.entity.OrderDetails;

public interface PaymentService {

    boolean verifyPayment(String orderId, String paymentId, String razorpaySignature);

    OrderDetails createOrder(Long productId);

    boolean updatePaymentDetails(String orderId, String paymentId, String signature);

}
