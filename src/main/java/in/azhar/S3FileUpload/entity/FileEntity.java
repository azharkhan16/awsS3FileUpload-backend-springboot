package in.azhar.S3FileUpload.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;        // to associate file with a course
    private String fileName;      // unique name (UUID)
    private String originalName;  // original file name
    private String url;
    private String contentType;
    private long size;
    private LocalDateTime createdAt;
}
