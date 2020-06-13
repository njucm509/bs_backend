package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.File;
import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;
import cn.edu.njucm.wp.bs.data.service.FieldService;
import cn.edu.njucm.wp.bs.data.service.FileService;
import cn.edu.njucm.wp.bs.data.vo.FileParam;
import cn.edu.njucm.wp.bs.data.vo.ReleasedVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FieldService fieldService;

    @GetMapping("page")
    public ResponseEntity<PageResult<File>> list(PageParam param) {
        PageResult<File> result = fileService.getFileByPage(param);
        if (result == null | result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping("create")
    public ResponseEntity<String> create(@RequestBody File file) {
        String msg = "";
        try {
            msg = fileService.create(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(msg);
    }

    @RequestMapping("delete")
    public ResponseEntity<Boolean> delete(@RequestBody File file) {

        System.out.println(file);
        return ResponseEntity.ok(fileService.delete(file));
    }

    @GetMapping("user/{id}")
    public ResponseEntity<File> getFileByUserId(@PathVariable("id") Long id) {
        File file = fileService.getFileByUserId(id);
        if (file == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(file);
    }

    @RequestMapping("hdfs/upload")
    public ResponseEntity<HashMap<String, String>> handleFileUpload(MultipartFile file) {
        System.out.println(file.getOriginalFilename());

        HashMap<String, String> res = null;
        try {
            res = fileService.handleFileUpload(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        File f = new File();

//        System.out.println(id);
//        if (res.get("msg").endsWith("文件上传成功!")) {
//            f.setName(file.getOriginalFilename());
//            f.setUserId(id);
//            f.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
//            f.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
//            fileService.create(f);
//        }

        return ResponseEntity.ok(res);
    }

    @PostMapping("hdfs/down")
    public ResponseEntity<java.io.File> downFileFromHDFS(@RequestBody File file) {
        java.io.File tmp = null;
        try {
            tmp = fileService.downFileFromHDFS(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(tmp);
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

    @PostMapping("data")
    public ResponseEntity<List<Map<String, Object>>> getUserData(@RequestBody FileParam param) {
        System.out.println(param);

        Long id = 0l;
        if (param.getId() != null) {
            id = param.getId();
        }

        List<Field> fields;
        if (param.getFields() != null) {
            fields = param.getFields();
        } else {
            fields = fieldService.getFieldByUserId(id);
        }

        List<UploadDataRecord> date = param.getDate();

        System.out.println(date);

        int type = 0;
        if (param.getType() != 0) {
            type = param.getType();
        }

        List<String> cols = fields.stream().map(Field::getSysName).distinct().collect(Collectors.toList());
        System.out.println(cols);
        List<Map<String, Object>> list = fileService.getDataByUserId(cols, id, type, date);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("data/{id}")
    public ResponseEntity<List<Map<String, Object>>> getUserDataById(@PathVariable Long id) {
        List<Field> fields = fieldService.getFieldByUserId(id);
        List<String> cols = fields.stream().map(Field::getSysName).distinct().collect(Collectors.toList());
        List<Map<String, Object>> list = fileService.getDataByUserId(cols, id);
        System.out.println(list);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("data/idAndDate")
    public ResponseEntity<List<Map<String, Object>>> getUserDataByIdAndDate(@RequestBody HashMap<String, Object> body) {
        Long id = Long.parseLong(String.valueOf(body.get("id")));
        List<String> date = (List<String>) body.get("curDate");
        System.out.println(date);
        List<Field> fields = fieldService.getFieldByUserId(id);
        List<String> cols = fields.stream().map(Field::getSysName).distinct().collect(Collectors.toList());
        List<Map<String, Object>> res = fileService.getDataByIdAndDate(cols, id, 0, date);

        return ResponseEntity.ok(res);
    }

    @PostMapping("anonymous/import")
    public ResponseEntity<List<Long>> insert2db(@RequestBody HashMap<String, Object> body) {
        String cols = (String) body.get("cols");
        String curDate = (String) body.get("curDate");
        Long userId = Long.parseLong(String.valueOf(body.get("userId")));
        List<String> items = (List<String>) body.get("items");
        if (StringUtils.isBlank(cols) || CollectionUtils.isEmpty(items)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(fileService.insert2db(cols, items, userId, curDate));
    }

    @GetMapping("ids")
    public ResponseEntity<List<Long>> getIdsByDateAndUserId(@RequestParam("table") String table, @RequestParam("date") String date, @RequestParam("userId") Long userId) {
        List<Long> ids = fileService.getIdsByDateAndUserId(table, date, userId);

        return ResponseEntity.ok(ids);
    }

    @PostMapping("released")
    public ResponseEntity<List<Map<String, Object>>> getDataByReleased(@RequestBody ReleasedVO releasedVO) {
        System.out.println(releasedVO);

        return ResponseEntity.ok(fileService.getDataByReleased(releasedVO));
    }
}
