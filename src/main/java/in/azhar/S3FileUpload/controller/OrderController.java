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
                                    @RequestHeader("userId") Long userId) {

        return ResponseEntity.ok(service.createOrder(courseId, userId));
    }

    // payment success callback
    @PostMapping("/success")
    public ResponseEntity<?> success(@RequestParam Long orderId,
                                     @RequestParam String paymentId) {

        return ResponseEntity.ok(service.markSuccess(orderId, paymentId));
    }
}
