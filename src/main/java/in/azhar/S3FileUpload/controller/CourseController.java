package in.azhar.S3FileUpload.controller;

import in.azhar.S3FileUpload.entity.Course;
import in.azhar.S3FileUpload.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService service;

    // ADMIN access only
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Course course) {
        return ResponseEntity.ok(service.create(course));
    }

    // USER + ADMIN access
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // USER + ADMIN access
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ADMIN access only
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Course course) {
        return ResponseEntity.ok(service.update(id, course));
    }

    // ADMIN access only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}