package cn.edu.njucm.wp.bs.data.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface FileService {
    PageResult<File> getFileByPage(PageParam param);

    HashMap<String, String> handleFileUpload(MultipartFile file) throws IOException;

    List<HashMap<String, Object>> listFile() throws IOException;

    File getFileByUserId(Long id);

    HashMap<String, String> handleFileUpload(MultipartFile file, Long id) throws IOException;

    Boolean create(File file);

    byte[] downFileFromHDFS(File file);
}
