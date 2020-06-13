package cn.edu.njucm.wp.bs.data.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.File;
import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;
import cn.edu.njucm.wp.bs.data.vo.ReleasedVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FileService {
    PageResult<File> getFileByPage(PageParam param);

    HashMap<String, String> handleFileUpload(MultipartFile file) throws IOException;

    List<HashMap<String, Object>> listFile() throws IOException;

    File getFileByUserId(Long id);

    HashMap<String, String> handleFileUpload(MultipartFile file, Long id) throws IOException;

    String create(File file) throws IOException;

    java.io.File downFileFromHDFS(File file) throws IOException;

    Boolean delete(File file);

    List<Map<String, Object>> getDataByUserId(List<String> cols, Long id);

    Integer insert2db(String cols, List<String> items);

    List<Map<String, Object>> getDataByUserId(List<String> cols, Long id, int type);

    List<Map<String, Object>> getDataByUserId(List<String> cols, Long id, int type, List<UploadDataRecord> date);

    List<Map<String, Object>> getDataByIdAndDate(List<String> cols, Long id, int type, List<String> date);

    List<Long> insert2db(String cols, List<String> items, Long userId, String curDate);

    List<Long> getIdsByDateAndUserId(String table, String date, Long userId);

    List<Map<String, Object>> getDataByUserId(String table, Long id, List<String> date);

    List<Map<String, Object>> getDataByReleased(ReleasedVO releasedVO);
}
