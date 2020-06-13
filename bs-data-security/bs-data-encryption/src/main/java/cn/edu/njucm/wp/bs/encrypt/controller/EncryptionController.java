package cn.edu.njucm.wp.bs.encrypt.controller;

import cn.edu.njucm.wp.bs.encrypt.service.EncryptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class EncryptionController {

    @Autowired
    EncryptionService encryptionService;

//    @RequestMapping("/encrypt")
//    public List<List<String>> encrypt(@RequestBody FileParam param) {
//        log.info("filename: {} --list: {}", param.getFilename(), param.getList());
//        List<List<String>> res = null;
//        try {
//            res = encryptionService.encrypt(param);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }

    @RequestMapping("he")
    public ResponseEntity<Boolean> encryptByHe(@RequestParam("id") Long userId, @RequestParam("fileId") Integer fileId, @RequestParam("filename") String filename) {
        Boolean flag = encryptionService.encryptByHe(userId, fileId, filename);

        return ResponseEntity.ok(flag);
    }

    @GetMapping("search")
    public ResponseEntity<List<String>> searchByHe(@RequestParam("key") String key) {
        System.out.println(key);

        List<String> list = encryptionService.searchByHe(key);

        return ResponseEntity.ok(list);
    }
}
