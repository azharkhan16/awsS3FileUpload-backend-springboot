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
                                    @RequestHeader("userId") Long userId) throws Exception {
        // initially we create order with PENDING status, after payment success we will mark it as SUCCESS
        // here we are hardcoding it for simplicity ,later we can fetch course price and pass it to service layer
        return ResponseEntity.ok(service.createOrder(userId, courseId, 500)); // dummy amount 500
    }

    // payment success callback or to verify payment success
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam Long orderId,
                                     @RequestParam String paymentId) {

        return ResponseEntity.ok(service.markSuccess(orderId, paymentId));
    }
}
