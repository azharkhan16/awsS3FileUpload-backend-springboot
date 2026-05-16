package in.azhar.S3FileUpload.auth;

import in.azhar.S3FileUpload.dto.AuthRequest;
import in.azhar.S3FileUpload.dto.AuthResponse;
import in.azhar.S3FileUpload.entity.Role;
import in.azhar.S3FileUpload.entity.User;
import in.azhar.S3FileUpload.exception.InvalidCredentialsException;
import in.azhar.S3FileUpload.repository.UserRepository;
import in.azhar.S3FileUpload.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public String register(@Valid @RequestBody AuthRequest request) {

        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists! Try with different email-ID");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        repo.save(user);

        return "User registered successfully";
    }


    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {

        User user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!user.isActive()) {
            throw new RuntimeException("User is deactivated");
        }

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token);
    }
}
