package in.azhar.S3FileUpload.service;

import in.azhar.S3FileUpload.entity.Course;
import in.azhar.S3FileUpload.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repo;

    public Course create(Course course) {
        return repo.save(course);
    }

    public List<Course> getAll() {
        return repo.findAll();
    }

    public Course getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course update(Long id, Course updated) {

        Course course = getById(id);

        course.setTitle(updated.getTitle());
        course.setDescription(updated.getDescription());
        course.setPrice(updated.getPrice());
        course.setFree(updated.isFree());

        return repo.save(course);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
