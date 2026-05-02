package in.azhar.S3FileUpload.service;

import in.azhar.S3FileUpload.entity.Course;
import in.azhar.S3FileUpload.entity.Order;
import in.azhar.S3FileUpload.entity.Status;
import in.azhar.S3FileUpload.entity.User;
import in.azhar.S3FileUpload.repository.CourseRepository;
import in.azhar.S3FileUpload.repository.OrderRepository;
import in.azhar.S3FileUpload.repository.UserRepository;
import in.azhar.S3FileUpload.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private OrderRepository repo;

    @Autowired
    private RazorpayService razorpayService;

    // creating order (before payment)
    public Order createOrder(Long courseId, String authHeader) throws Exception {

        // 1 -> Extracting user from JWT
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2 -> Getting  course price
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        int amount = (int) course.getPrice();

        // creating razorpay order
        com.razorpay.Order razorpayOrder = razorpayService.createRazorpayOrder(amount);

        Order order = new Order();
        order.setUserId(user.getId());
        order.setCourseId(courseId);
        order.setStatus(Status.PENDING);
        order.setOrderId(razorpayOrder.get("id"));

        return repo.save(order);
    }


    public Order verifyPayment(Long orderDbId,
                               String razorpayOrderId,
                               String paymentId,
                               String signature) {

        Order order = repo.findById(orderDbId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // signature verify
        boolean isValid = razorpayService.verifyPayment(
                razorpayOrderId,
                paymentId,
                signature
        );
        if (!isValid) {
            throw new RuntimeException("Invalid payment signature ");
        }
        //  success
        order.setPaymentId(paymentId);
        order.setStatus(Status.SUCCESS);

        return repo.save(order);
    }

    // check access
    public boolean hasAccess(Long userId, Long courseId) {

        return repo.findByUserIdAndCourseIdAndStatus(userId, courseId, Status.SUCCESS)
                .isPresent();
    }
}