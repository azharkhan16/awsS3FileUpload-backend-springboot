package in.azhar.S3FileUpload.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}