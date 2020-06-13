package cn.edu.njucm.wp.bs.encrypt.service;

import cn.edu.njucm.wp.bs.params.FileParam;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface EncryptionService extends Serializable {
    List<List<String>> encrypt(FileParam param) throws IOException;

    Boolean encryptByHe(Long userId, Integer fileId, String filename);

    List<String> searchByHe(String key);
}

