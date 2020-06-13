package cn.edu.njucm.wp.bs.data.controller;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.Type;
import cn.edu.njucm.wp.bs.data.service.FieldService;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("field")
public class FieldController {

    @Autowired
    FieldService fieldService;

    @PostMapping("create")
    public ResponseEntity<Boolean> create(@RequestBody Field field) {
        Boolean flag = fieldService.check(field);
        if (flag) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(fieldService.create(field));
    }

    private Boolean check(Field field) {
        return fieldService.check(field);
    }

    @PostMapping("update")
    public ResponseEntity<Boolean> update(@RequestBody Field field) {
        System.out.println(field);
        Boolean flag = fieldService.check(field);
        if (flag) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(fieldService.update(field));
    }

    @GetMapping("init")
    public ResponseEntity<Boolean> init() {
        String tableName = "init_data_all_field";
        Boolean flag = fieldService.checkTable(tableName);
        Boolean res = false;
        if (flag) {
            res = fieldService.updateTableScheme();
        } else {
            res = fieldService.init(tableName, 0);
        }

        tableName = "init_data_all_field_k";
        flag = fieldService.checkTable(tableName);
        Boolean res_2 = false;
        if (flag) {
            res_2 = fieldService.updateTableScheme();
        } else {
            res_2 = fieldService.init(tableName, 1);
        }

        return ResponseEntity.ok(res && res_2);
    }

    @GetMapping("drop")
    public ResponseEntity<Boolean> drop() {
        List<String> list = new ArrayList<>();
        list.add("init_data_all_field");
        list.add("init_data_all_field_k");
        fieldService.dropInitTable(list);

        return ResponseEntity.ok(true);
    }

    @GetMapping("page")
    public ResponseEntity<PageResult<Field>> list(PageParam param) {
        PageResult<Field> result = fieldService.getFieldByPageAndSort(param);
        if (result == null | result.getItems().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("list")
    public ResponseEntity<List<Field>> list() {
        List<Field> list = fieldService.list();
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<Field>> getFieldByUserId(@PathVariable("id") Long id) {
        List<Field> fields = fieldService.getFieldByUserId(id);
        if (CollectionUtils.isEmpty(fields)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(fields);
    }

    @GetMapping("export/{type}/{id}")
    public ResponseEntity<byte[]> export(@PathVariable String type, @PathVariable("id") Long id) {
        ResponseEntity<byte[]> res = null;
        List<Field> fields = fieldService.getFieldByUserId(id);
        Collections.sort(fields, (v1, v2) -> (int) (v1.getId() - v2.getId()));
        if (type.equals("csv")) {
            StringBuilder colNames = new StringBuilder();
            for (Field field : fields) {
                colNames.append(field.getSysName() + ",");
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "field.csv");
            try {
                File tempFile = File.createTempFile("field", "csv");
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
                bw.write(colNames.toString());
                bw.flush();
                bw.close();
                res = new ResponseEntity<>(FileUtils.readFileToByteArray(tempFile), headers, HttpStatus.CREATED);
                boolean b = tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (type.equals("excel")) {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("field");
            HSSFRow row = sheet.createRow(0);
            int i = 0;
            for (Field field : fields) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(field.getSysName());
                System.out.println(cell.getStringCellValue());
                i++;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("field.xls").build());
            headers.setContentType(MediaType.valueOf("application/msexcel"));
            try {
                File tempFile = File.createTempFile("field", "xls");
                wb.write(tempFile);
                wb.close();
                res = new ResponseEntity<>(FileUtils.readFileToByteArray(tempFile), headers, HttpStatus.CREATED);
                boolean b = tempFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;

    }

    @GetMapping("type")
    public ResponseEntity<List<Type>> getTypeList() {
        List<Type> list = fieldService.getTypeList();
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("ids")
    public ResponseEntity<List<Field>> getByIds(@RequestParam("ids") String ids) {
        List<Field> list = fieldService.getFieldByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(list);
    }
}
