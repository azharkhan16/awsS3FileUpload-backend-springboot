package in.azhar.S3FileUpload.repository;

import in.azhar.S3FileUpload.entity.Order;
import in.azhar.S3FileUpload.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserIdAndCourseIdAndStatus(Long userId, Long courseId, Status status);

}
