package in.azhar.S3FileUpload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {

    private String fileName;
    private String url;
    private long size;
}
