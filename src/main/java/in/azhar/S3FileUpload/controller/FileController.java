package in.azhar.S3FileUpload.controller;

import in.azhar.S3FileUpload.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileService.upload(file));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> getFile(@PathVariable String fileName) {
        return ResponseEntity.ok(fileService.getFile(fileName));
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<?> delete(@PathVariable String fileName) {
        fileService.deleteFile(fileName);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/{fileName}")
    public ResponseEntity<?> update(@PathVariable String fileName,
                                    @RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileService.updateFile(fileName, file));
    }
}