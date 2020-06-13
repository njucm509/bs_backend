package cn.edu.njucm.wp.bs.encrypt.service;

import cn.edu.njucm.wp.bs.encrypt.entity.EnvInfo;
import org.apache.zookeeper.Environment;
import org.apache.zookeeper.Version;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class EnvInfoService {

    public EnvInfo init() throws UnknownHostException {
        return EnvInfo.builder()
                .zkVersion(Version.getFullVersion())
                .hostname(InetAddress.getLocalHost().getCanonicalHostName())
                .javaVersion(System.getProperty("java.version", "<NA>"))
                .javaVendor(System.getProperty("java.vendor", "<NA>"))
//                .javaHome(System.getProperty("java.home", "<NA>"))
//                .javaClass(System.getProperty("java.class.path", "<NA>"))
                .javaLibPath(System.getProperty("java.library.path", "<NA>"))
                .javaIOTmpDir(System.getProperty("java.io.tmpdir", "<NA>"))
                .javaCompiler(System.getProperty("java.compiler", "<NA>"))
                .osName(System.getProperty("os.name", "<NA>"))
                .osArch(System.getProperty("os.arch", "<NA>"))
                .osVersion(System.getProperty("os.version", "<NA>"))
                .username(System.getProperty("user.name", "<NA>"))
                .userHome(System.getProperty("user.home", "<NA>"))
                .userDir(System.getProperty("user.dir", "<NA>"))
                .build();
    }

}
