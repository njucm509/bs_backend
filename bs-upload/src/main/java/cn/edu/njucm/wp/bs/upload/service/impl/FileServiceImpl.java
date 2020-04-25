package cn.edu.njucm.wp.bs.upload.service.impl;

import cn.edu.njucm.wp.bs.upload.config.HDFSConfig;
import cn.edu.njucm.wp.bs.upload.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private static final String SEPARATOR = File.separator;

    @Autowired
    FileSystem fileSystem;

    @Override
    public HashMap<String, String> handleFileUpload(MultipartFile file) throws IOException {
        String userFilePath = HDFSConfig.userFilePath;
        if (userFilePath == null) {
            userFilePath = "/input";
        }
        HashMap<String, String> res = new HashMap<>();
        Path path = new Path(userFilePath);
        if (!fileSystem.exists(path)) {
            fileSystem.mkdirs(path);
        }
        Path filePath = new Path(userFilePath + SEPARATOR + file.getOriginalFilename());
        if (fileSystem.exists(filePath)) {
            res.put("msg", "文件已存在!");
            return res;
        }
        FSDataOutputStream fos = fileSystem.create(filePath, true);
        IOUtils.copyBytes(file.getInputStream(), fos, 4096, true);

        res.put("msg", file.getOriginalFilename() + " 文件上传成功!");

        return res;
    }

    @Override
    public List<HashMap<String, Object>> listFile() throws IOException {
        String userFilePath = HDFSConfig.userFilePath;
        if (userFilePath == null) {
            userFilePath = "/input";
        }
        Path path = new Path(userFilePath);
        if (!fileSystem.exists(path)) {
            fileSystem.mkdirs(path);
        }
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(path, false);
        List<HashMap<String, Object>> res = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        if (!iterator.hasNext()) {
            map.put("msg", "no");
            res.add(map);
            return res;
        }
        int i = 1;
        while (iterator.hasNext()) {
            LocatedFileStatus fileStatus = iterator.next();
            Path filePath = fileStatus.getPath();
            log.info("file path: {}", filePath.getName());
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", i++);
            data.put("filename", filePath.getName());
            res.add(data);
        }
        return res;
    }
}
