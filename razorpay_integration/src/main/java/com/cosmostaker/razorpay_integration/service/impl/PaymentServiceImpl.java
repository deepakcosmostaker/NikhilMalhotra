package com.cosmostaker.razorpay_integration.service.impl;

import com.cosmostaker.razorpay_integration.entity.OrderDetails;
import com.razorpay.Order;
import com.cosmostaker.razorpay_integration.entity.Product;
import com.cosmostaker.razorpay_integration.entity.User;
import com.cosmostaker.razorpay_integration.repository.OrderRepository;
import com.cosmostaker.razorpay_integration.repository.ProductRepository;
import com.cosmostaker.razorpay_integration.repository.UserRepository;
import com.cosmostaker.razorpay_integration.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentServiceImpl implements PaymentService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RazorpayClient razorpayClient;


    @Override
    public boolean verifyPayment(String orderId, String paymentId, String razorpaySignature) {
        String payload = orderId + '|' + paymentId;
        try {
            return Utils.verifySignature(payload, razorpaySignature, "Z5Cb7p6UOhMzwEdziYR44R48");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OrderDetails createOrder(Long productId)  {
        Product product = (Product) productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        //using a same user for now
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new orderDetails details in the database
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setUser(user);
        orderDetails.setProduct(product);
        orderDetails.setStatus("PENDING");
        orderRepository.save(orderDetails);

        // Interact with Razorpay to create an order
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", product.getPrice() * 100);
        orderRequest.put("currency", "INR");
        String receipt = "ord_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).toString();
        orderRequest.put("receipt", receipt);
        Order razorpayOrderResponse =  null;
        try {
             razorpayOrderResponse = razorpayClient.orders.create(orderRequest);
        }
        catch (Exception e){
            System.out.println("some error occured"+e);
        }
        // Get Razorpay Order ID from the response
        String razorpayOrderId = razorpayOrderResponse.get("id");
        orderDetails.setOrderId(razorpayOrderId);  // Set the payment ID in the orderDetails object
        orderDetails.setStatus("CREATED");

        // Save the orderDetails with Razorpay payment ID
        orderRepository.save(orderDetails);
        return orderDetails;
    }

    @Override
    public boolean updatePaymentDetails(String orderId, String paymentId, String signature) {
        // Fetch the order details using the provided orderId
        OrderDetails orderDetails = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Verify the payment signature
        if (verifyPayment(orderDetails.getOrderId(), paymentId, signature)) {
            // Update the order with payment details and change the status to SUCCESS
            orderDetails.setPaymentId(paymentId);
            orderDetails.setSignature(signature);
            orderDetails.setStatus("SUCCESS");

            // Save the updated order details
            orderRepository.save(orderDetails);
            return true;
        }
        return false;
    }
}