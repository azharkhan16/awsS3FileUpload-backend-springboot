package in.azhar.S3FileUpload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileDownloadResponse {

    private String fileName;
    private String originalName;
    private long size;
    private String contentType;
    private String downloadUrl;

}
