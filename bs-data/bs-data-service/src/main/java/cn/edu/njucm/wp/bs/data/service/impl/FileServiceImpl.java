package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.config.HDFSConfig;
import cn.edu.njucm.wp.bs.data.mapper.*;
import cn.edu.njucm.wp.bs.data.pojo.*;
import cn.edu.njucm.wp.bs.data.pojo.File;
import cn.edu.njucm.wp.bs.data.service.FieldService;
import cn.edu.njucm.wp.bs.data.service.FileService;
import cn.edu.njucm.wp.bs.data.service.ReleasedService;
import cn.edu.njucm.wp.bs.data.vo.ReleasedVO;
import cn.edu.njucm.wp.bs.util.FileArgGenerater;
import cn.edu.njucm.wp.bs.util.IDConvert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private static final String SEPARATOR = java.io.File.separator;

    @Autowired
    FileSystem fileSystem;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FieldService fieldService;

    @Autowired
    ReleasedService releasedService;

    @Autowired
    UploadDataRecordMapper uploadDataRecordMapper;

    @Autowired
    FileArgMapper fileArgMapper;

    @Autowired
    ReleasedDataApplyMapper releasedDataApplyMapper;

    @Autowired
    ReleasedRecordMapper releasedRecordMapper;

    @Override
    public PageResult<File> getFileByPage(PageParam param) {
        Example example = new Example(File.class);
        Example.Criteria criteria = example.createCriteria();
        if (param.getUserId() != null) {
            criteria.andEqualTo("userId", param.getUserId());
        }

        if (param.getEnc() != null && param.getEnc().equals(1)) {
            criteria.andEqualTo("encAlgId", '6');
        }

        if (StringUtils.isNotBlank(param.getKey())) {
            criteria.andLike("name", "%" + param.getKey() + "%");
        }

        if (StringUtils.isNotBlank(param.getSortBy())) {
            example.setOrderByClause(param.getSortBy() + (param.getDesc() ? " DESC" : " ASC"));
        }

        PageHelper.startPage(param.getPage(), param.getRows());

        Page<File> page = (Page<File>) fileMapper.selectByExample(example);

        return new PageResult<>(page.getTotal(), page);
    }

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
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", i++);
            data.put("filename", filePath.getName());
            res.add(data);
        }
        return res;
    }

    @Override
    public File getFileByUserId(Long id) {
        return fileMapper.getFileByUserId(id);
    }

    @Override
    public HashMap<String, String> handleFileUpload(MultipartFile file, Long id) throws IOException {
        String userFilePath = HDFSConfig.userFilePath;
        if (userFilePath == null) {
            userFilePath = "/input/_" + id;
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
//    @Transactional
    public String create(File file) throws IOException {
        if (file.getCreatedAt() == null) {
            file.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        if (file.getUpdatedAt() == null) {
            file.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        moveFile2UserPath(file.getName(), file.getUserId());
        String idStr = "";

        fileMapper.insertSelective(file);
        Integer id = file.getId();

        FileArg arg = null;
        try {
            arg = FileArgGenerater.initKey(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileArgMapper.insertSelective(arg);

        UploadDataRecord uploadDataRecord = new UploadDataRecord();
        if (file.getEncAlgId() == null) {
            String date = import2db(file);
            List<Long> ids = getIdsByDateAndUserId("init_data_all_field", date, file.getUserId());
            idStr = IDConvert.convert2db(ids);
            uploadDataRecord.setUploadDate(date);
            uploadDataRecord.setFileId(id);
            uploadDataRecord.setUserId(file.getUserId());
            uploadDataRecord.setUploadLines(idStr);
            uploadDataRecord.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            uploadDataRecord.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            uploadDataRecordMapper.insertSelective(uploadDataRecord);
        }

        if (file.getEncAlgId() != null && file.getEncAlgId().equals(6)) {
            return String.valueOf(id);
        } else {
            return idStr;
        }
    }

    private String import2db(File file) {
        List<Field> fields = fieldService.list();
        List<String> names = fields.stream().map(Field::getSysName).distinct().collect(Collectors.toList());
        if (file.getName().endsWith("csv")) {
            try {
                BufferedReader br = getFileFromHDFS(file);
                if (br == null) {
                    return null;
                }
                String line = br.readLine();
                String[] headers = line.split(",");
                String cols = Arrays.toString(headers);
                cols = "user_id,date, " + cols.substring(1, cols.length() - 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String nowDate = sdf.format(new Date());
                boolean b = names.containsAll(Arrays.asList(headers));
                if (!b) {
                    return null;
                }
                List<String> items = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    StringBuilder sb = new StringBuilder();
                    String[] ss = line.split(",");
                    for (String s : ss) {
                        sb.append(",'" + s + "'");
                    }
                    line = sb.toString();
                    items.add("(" + file.getUserId() + ",'" + nowDate + "'" + line + ")");
                }
                Integer res = fileMapper.insert2init(cols, items);
                return nowDate;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private BufferedReader getFileFromHDFS(File file) throws IOException {
        String userFilePath = HDFSConfig.userFilePath;
        userFilePath = "/input/_" + file.getUserId();
        String origin = userFilePath + SEPARATOR + file.getName();
        Path filePath = new Path(origin);
        if (!fileSystem.exists(filePath)) {
            return null;
        }
        FSDataInputStream open = fileSystem.open(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(open, "GBK"));
        return br;
    }

    private void moveFile2UserPath(String name, Long userId) throws IOException {
        String userFilePath = HDFSConfig.userFilePath;
        if (userFilePath == null) {
            userFilePath = "/input";
        }

        String origin = userFilePath + SEPARATOR + name;
        Path filePath = new Path(origin);
        if (!fileSystem.exists(filePath)) {
            return;
        }

        userFilePath += "/_" + userId;
        Path path = new Path(userFilePath);
        if (!fileSystem.exists(path)) {
            fileSystem.mkdirs(path);
        }
        Path newPath = new Path(userFilePath + SEPARATOR + name);
        FSDataOutputStream fos = fileSystem.create(newPath, true);
        FSDataInputStream open = fileSystem.open(filePath);
        IOUtils.copyBytes(open, fos, 4096, true);
        fileSystem.delete(filePath, true);
    }

    @Override
    public java.io.File downFileFromHDFS(File file) throws IOException {
        String name = file.getName();
        String[] ss = name.split("\\.");
        Long userId = file.getUserId();
        String userFilePath = HDFSConfig.userFilePath;
        if (userFilePath == null) {
            userFilePath = "/input";
        }

        userFilePath += "/_" + userId;
        Path path = new Path(userFilePath);
        if (!fileSystem.exists(path)) {
            return null;
        }

        String origin = userFilePath + SEPARATOR + name;
        Path filePath = new Path(origin);
        if (!fileSystem.exists(filePath)) {
            return null;
        }

        FSDataInputStream fis = fileSystem.open(new Path(origin));
        java.io.File tempFile = java.io.File.createTempFile(ss[0], ss[1]);
        IOUtils.copyBytes(fis, new FileOutputStream(tempFile), 4096, true);
        return tempFile;
    }
//@Override
//    public java.io.File downFileFromHDFS(File file) throws IOException {
//        String name = file.getName();
//        String[] ss = name.split("\\.");
//        Long userId = file.getUserId();
//        String userFilePath = HDFSConfig.userFilePath;
//        if (userFilePath == null) {
//            userFilePath = "/input";
//        }
//
//        userFilePath += "/_" + userId;
//        Path path = new Path(userFilePath);
//        if (!fileSystem.exists(path)) {
//            return null;
//        }
//
//        String origin = userFilePath + SEPARATOR + name;
//        Path filePath = new Path(origin);
//        if (!fileSystem.exists(filePath)) {
//            return null;
//        }
//
//        System.out.println(origin);
//
//        FSDataInputStream fis = fileSystem.open(new Path(origin));
//        java.io.File tempFile = java.io.File.createTempFile(ss[0], ss[1]);
//        IOUtils.copyBytes(fis, new FileOutputStream(tempFile), 4096, true);
//        return tempFile;
//    }

    @Override
    public Boolean delete(File file) {
        String msg;
        try {
            msg = deleteFromHdfs(file);
        } catch (IOException e) {
            return false;
        }

        if (msg != null && msg.equals("del")) {
            return fileMapper.deleteByPrimaryKey(file) == 1;
        } else {
            return false;
        }
    }

    private String deleteFromHdfs(File file) throws IOException {
        String filePath = HDFSConfig.userFilePath;
        if (filePath == null) {
            filePath = "/input";
        }

        String userFilePath = filePath + SEPARATOR + "_" + file.getUserId();
        Path path = new Path(userFilePath);
        if (!fileSystem.exists(path)) {
            return "no path";
        }

        String userFile = userFilePath + SEPARATOR + file.getName();
        Path f = new Path(userFile);
        if (!fileSystem.exists(f)) {
            return "no file";
        }

        boolean delete = fileSystem.delete(f, true);
        if (delete) {
            return "del";
        } else {
            return "fail";
        }
    }

    @Override
    public List<Map<String, Object>> getDataByUserId(List<String> cols, Long id) {
        return fileMapper.getDataByUserId(cols, id);
    }

    @Override
    public List<Map<String, Object>> getDataByUserId(List<String> cols, Long id, int type) {
        if (type == 1) {
            return getAnonymousDataByUserId(cols, id);
        } else {
            return getDataByUserId(cols, id);
        }
    }

    @Override
    public List<Map<String, Object>> getDataByUserId(List<String> cols, Long id, int type, List<UploadDataRecord> date) {
        List<String> list = date.stream().map(UploadDataRecord::getUploadDate).distinct().collect(Collectors.toList());

        return getDataByUserIdAndDate(cols, id, type, list);
    }

    @Override
    public List<Map<String, Object>> getDataByIdAndDate(List<String> cols, Long id, int type, List<String> date) {
        return getDataByUserIdAndDate(cols, id, type, date);
    }

    @Override
    public List<Long> insert2db(String cols, List<String> items, Long userId, String curDate) {
        List<Long> ids = new ArrayList<>();
        if (userId == null || curDate == null) {
            return new ArrayList<>();
        } else {
            Integer res = insert2db(cols, items);
            if (res > 0) {
                ids = getIdsByDateAndUserId("init_data_all_field_k", curDate, userId);
            }
        }
        return ids;
    }

    @Override
    public List<Long> getIdsByDateAndUserId(String table, String date, Long userId) {
        return fileMapper.getIdsByDateAndUserId(table, date, userId);
    }

    @Override
    public List<Map<String, Object>> getDataByUserId(String table, Long id, List<String> date) {
        return fileMapper.getDataByIdAndDate(table, id, date);
    }

    @Override
    public List<Map<String, Object>> getDataByReleased(ReleasedVO releasedVO) {
        String releasedData = releasedVO.getReleasedData();
        String[] ss = releasedData.split("\\|");
        Long id = Long.valueOf(ss[0]);
        String date = ss[1];

        ReleasedDataApply apply = new ReleasedDataApply();
        apply.setReleasedId(releasedVO.getId());
        apply.setHandleUserId(releasedVO.getUser().getId());
        ReleasedDataApply releasedDataApply = releasedDataApplyMapper.selectOne(apply);

        ReleasedRecord record = new ReleasedRecord();
        record.setReleasedId(releasedVO.getId());
        record.setHandleUserId(releasedVO.getUser().getId());
        record.setApplyUserId(releasedDataApply.getApplyUserId());
        record.setReleasedDataApplyId(releasedDataApply.getId());
        ReleasedRecord releasedRecord = releasedRecordMapper.selectOne(record);

        String canRead = releasedRecord.getCanRead();
        List<Field> fields = fieldService.getFieldByIds(canRead);

        List<String> cols = fields.stream().map(Field::getSysName).distinct().collect(Collectors.toList());

        return getDataByUserIdAndDate(cols, id, 1, Collections.singletonList(date));
    }

    private List<Map<String, Object>> getDataByUserIdAndDate(List<String> cols, Long id, int type, List<String> date) {
        if (type == 1) {
            return fileMapper.getAnonymousDataByUserIdAndDate(cols, id, date);
        } else {
            return fileMapper.getDataByUserIdAndDate(cols, id, date);
        }
    }

    private List<Map<String, Object>> getAnonymousDataByUserId(List<String> cols, Long id) {
        return fileMapper.getAnonymousDataByUserId(cols, id);
    }

    @Override
    public Integer insert2db(String cols, List<String> items) {
        return fileMapper.insert2db(cols, items);
    }

    public static void main(String[] args) {
        String test = "xx,asd,ca,,asd,,,,";
        test = "[JGDM, MZH, KSMC, HZXM, XB, CSRQ, AGE, GMS, ZS, XBS, JWS, TGJC, YSQM]";
        System.out.println("user_id, " + test.substring(1, test.length() - 1));
        System.out.println(Arrays.toString(test.split(",")));
    }
}
