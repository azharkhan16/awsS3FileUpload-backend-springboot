package in.azhar.S3FileUpload.controller;

import in.azhar.S3FileUpload.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    // create order
    @PostMapping("/{courseId}")
    public ResponseEntity<?> create(@PathVariable Long courseId,
                                    @RequestHeader("Authorization") String authHeader) throws Exception {
        return ResponseEntity.ok(service.createOrder(courseId, authHeader));
    }

    // payment success callback or to verify payment success
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam Long orderId,
                                    @RequestParam String razorpayOrderId,
                                    @RequestParam String paymentId,
                                    @RequestParam String signature) {

        return ResponseEntity.ok(
                service.verifyPayment(orderId, razorpayOrderId, paymentId, signature)
        );
    }
}
