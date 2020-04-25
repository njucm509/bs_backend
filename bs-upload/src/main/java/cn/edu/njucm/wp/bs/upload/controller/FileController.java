package cn.edu.njucm.wp.bs.upload.controller;

import cn.edu.njucm.wp.bs.upload.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/upload")
public class FileController {


    @Autowired
    FileService fileService;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @RequestMapping("/file")
    public HashMap<String, String> handleFileUpload(MultipartFile file) {
        log.info("{} enter...", file.getOriginalFilename());
        HashMap<String, String> res = null;
        try {
            res = fileService.handleFileUpload(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 返回文件列表
     *
     * @return
     */
    @RequestMapping("/file/list")
    public List<HashMap<String, Object>> listFile() {
        List<HashMap<String, Object>> res = null;
        try {
            res =  fileService.listFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
