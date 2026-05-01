package in.azhar.S3FileUpload.service;

import in.azhar.S3FileUpload.entity.Order;
import in.azhar.S3FileUpload.entity.Status;
import in.azhar.S3FileUpload.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repo;

    @Autowired
    private RazorpayService razorpayService;

    // creating order (before payment)
    public Order createOrder(Long userId, Long courseId, int amount) throws Exception {

        // creating razorpay order
        com.razorpay.Order razorpayOrder = razorpayService.createRazorpayOrder(amount);

        Order order = new Order();
        order.setUserId(userId);
        order.setCourseId(courseId);
        order.setStatus(Status.PENDING);
        order.setOrderId(razorpayOrder.get("id"));

        return repo.save(order);
    }

    // marking payment success
    public Order markSuccess(Long orderId, String paymentId) {

        Order order = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(Status.SUCCESS);
        order.setPaymentId(paymentId);

        return repo.save(order);
    }

    // check access
    public boolean hasAccess(Long userId, Long courseId) {

        return repo.findByUserIdAndCourseIdAndStatus(userId, courseId, Status.SUCCESS)
                .isPresent();
    }
}