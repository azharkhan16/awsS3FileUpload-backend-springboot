package in.azhar.S3FileUpload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import in.azhar.S3FileUpload.dto.FileResponse;
import in.azhar.S3FileUpload.entity.FileEntity;
import in.azhar.S3FileUpload.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private FileRepository fileRepository;

    @Value("${amazon.s3.bucket}")
    private String bucket;

    public FileResponse upload(MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalName;

        File convertedFile = new File(originalName);
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, convertedFile));
        convertedFile.delete();

        String url = amazonS3.getUrl(bucket, fileName).toString();

        FileEntity entity = new FileEntity();
        entity.setFileName(fileName);
        entity.setOriginalName(originalName);
        entity.setUrl(url);
        entity.setContentType(file.getContentType());
        entity.setSize(file.getSize());
        entity.setCreatedAt(LocalDateTime.now());

        fileRepository.save(entity);

        return new FileResponse(fileName, url, file.getSize());
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public FileEntity getFile(String fileName) {
        return fileRepository.findByFileName(fileName)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    public void deleteFile(String fileName) {
        FileEntity file = getFile(fileName);

        amazonS3.deleteObject(bucket, fileName);
        fileRepository.delete(file);
    }

    public FileResponse updateFile(String fileName, MultipartFile newFile) throws IOException {

        FileEntity oldFile = getFile(fileName);

        amazonS3.deleteObject(bucket, fileName);

        String newFileName = UUID.randomUUID() + "_" + newFile.getOriginalFilename();

        File convertedFile = new File(newFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(newFile.getBytes());
        }

        amazonS3.putObject(bucket, newFileName, convertedFile);
        convertedFile.delete();

        String url = amazonS3.getUrl(bucket, newFileName).toString();

        oldFile.setFileName(newFileName);
        oldFile.setUrl(url);
        oldFile.setSize(newFile.getSize());
        oldFile.setContentType(newFile.getContentType());

        fileRepository.save(oldFile);

        return new FileResponse(newFileName, url, newFile.getSize());
    }
}