package cn.edu.njucm.wp.bs.data.api;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.File;
import cn.edu.njucm.wp.bs.data.pojo.Released;
import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataApi {
    @GetMapping("/field/all/{id}")
    public List<Field> getFieldByUserId(@PathVariable("id") Long id);

    @GetMapping("/field/ids")
    public List<Field> getByIds(@RequestParam("ids") String ids);

    @GetMapping("/file/data/{id}")
    public List<Map<String, Object>> getUserDataById(@PathVariable("id") Long id);

    @PostMapping("/file/anonymous/import")
    public List<Long> insert2db(@RequestBody HashMap<String, Object> body);

    @GetMapping("/file/ids")
    public List<Long> getIdsByDateAndUserId(@RequestParam("table") String table, @RequestParam("date") String date, @RequestParam("userId") Long userId);

    @PostMapping("/released/create")
    public Integer create(@RequestBody Released released);

    @PostMapping("/file/data/idAndDate")
    public List<Map<String, Object>> getUserDataByIdAndDate(@RequestBody HashMap<String, Object> body);

    @PostMapping("/file/hdfs/down")
    public java.io.File downFileFromHDFS(@RequestBody File file);

    @RequestMapping("/file/arg/{id}")
    public HashMap<String, String> getFileArg(@PathVariable("id") Integer id);
}
