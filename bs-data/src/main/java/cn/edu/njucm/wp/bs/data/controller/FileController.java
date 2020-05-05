package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.File;
import cn.edu.njucm.wp.bs.data.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("page")
    public ResponseEntity<PageResult<File>> list(PageParam param) {
        PageResult<File> result = fileService.getFileByPage(param);
        if (result == null | result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping("create")
    public ResponseEntity<Boolean> create(File file) {

        return ResponseEntity.ok(true);
    }

    @RequestMapping("delete")
    public ResponseEntity<Boolean> delete(File file) {

        return ResponseEntity.ok(true);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<File> getFileByUserId(@PathVariable("id") Long id) {
        File file = fileService.getFileByUserId(id);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok()
    }

    @RequestMapping("hdfs/upload")
    public ResponseEntity<HashMap<String, String>> handleFileUpload(MultipartFile file) {
        HashMap<String, String> res = null;
        try {
            res = fileService.handleFileUpload(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(res);
    }

    @RequestMapping("hdfs/list")
    public ResponseEntity<List<HashMap<String, Object>>> listFile() {
        List<HashMap<String, Object>> res = null;
        try {
            res = fileService.listFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(res);
    }
}
