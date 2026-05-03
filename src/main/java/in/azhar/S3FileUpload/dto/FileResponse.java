package in.azhar.S3FileUpload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {

    private String fileName;
//  private String url;   we are not sending url in response, due to security concerns.Instead, we send presigned URL.
    private long size;
}
