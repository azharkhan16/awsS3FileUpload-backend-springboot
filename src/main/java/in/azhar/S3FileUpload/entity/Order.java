package in.azhar.S3FileUpload.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long courseId;

    private String paymentId; // for razorpay payment id
    private String orderId;   // for razorpay order id

    @Enumerated(EnumType.STRING)
    private Status status;   // PENDING, SUCCESS, FAILED

    private LocalDateTime createdAt = LocalDateTime.now();
}
