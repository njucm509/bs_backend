package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.config.HDFSConfig;
import cn.edu.njucm.wp.bs.data.mapper.FileMapper;
import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.File;
import cn.edu.njucm.wp.bs.data.service.FileService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private static final String SEPARATOR = java.io.File.separator;

    @Autowired
    FileSystem fileSystem;

    @Autowired
    FileMapper fileMapper;

    @Override
    public PageResult<File> getFileByPage(PageParam param) {
        PageHelper.startPage(param.getPage(), param.getRows());
        Example example = new Example(File.class);
        example.createCriteria().andEqualTo("userId", param.getUserId());
        if (StringUtils.isNotBlank(param.getKey())) {
            example.createCriteria().andLike("name", "%" + param.getKey() + "%");
        }
        if (StringUtils.isNotBlank(param.getSortBy())) {
            example.setOrderByClause(param.getSortBy() + (param.getDesc() ? " DESC" : " ASC"));
        }
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
    public Boolean create(File file) {
        if (file.getCreatedAt() == null) {
            file.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        if (file.getUpdatedAt() == null) {
            file.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        return fileMapper.insertSelective(file) == 1;
    }

    @Override
    public byte[] downFileFromHDFS(File file) {
        return new byte[0];
    }

    @Override
    public Boolean delete(File file) {
        return fileMapper.deleteByPrimaryKey(file) == 1;
    }

}
