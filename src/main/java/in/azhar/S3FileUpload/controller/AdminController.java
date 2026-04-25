package in.azhar.S3FileUpload.controller;

import in.azhar.S3FileUpload.entity.User;
import in.azhar.S3FileUpload.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository repo;

    // deactivate user
    @PutMapping("/deactivate/{id}")
    public String deactivate(@PathVariable Long id) {

        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false);
        repo.save(user);

        return "User deactivated";
    }

    // reactivate user
    @PutMapping("/activate/{id}")
    public String activate(@PathVariable Long id) {

        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(true);
        repo.save(user);

        return "User activated";
    }
}
