package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.data.mapper.FileArgMapper;
import cn.edu.njucm.wp.bs.data.pojo.FileArg;
import cn.edu.njucm.wp.bs.data.service.FileArgService;
import cn.edu.njucm.wp.bs.util.FileArgGenerater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class FileArgServiceImpl implements FileArgService {

    @Autowired
    FileArgMapper mapper;


    public HashMap<String, String> getFileArg(Integer id) {
        FileArg arg = new FileArg();
        arg.setFileId(id);

        FileArg f = mapper.selectOne(arg);

        return FileArgGenerater.getFileArg(f);

    }
}
