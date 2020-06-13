package cn.edu.njucm.wp.bs.encrypt.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnvInfo {
    private String zkVersion;
    private String hostname;
    private String javaVersion;
    private String javaVendor;
    private String javaHome;
    private String javaClass;
    private String javaLibPath;
    private String javaIOTmpDir;
    private String javaCompiler;
    private String osName;
    private String osArch;
    private String osVersion;
    private String username;
    private String userHome;
    private String userDir;
}
