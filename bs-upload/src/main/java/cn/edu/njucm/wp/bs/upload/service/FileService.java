package cn.edu.njucm.wp.bs.upload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface FileService {
    HashMap<String, String> handleFileUpload(MultipartFile file) throws IOException;

    List<HashMap<String, Object>> listFile() throws IOException;
}
