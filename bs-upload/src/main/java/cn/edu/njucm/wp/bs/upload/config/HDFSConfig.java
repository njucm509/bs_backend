package cn.edu.njucm.wp.bs.upload.config;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HDFSConfig {
    @Value("${hadoop.hdfs.uri}")
    private String uri;

    @Value("${hadoop.hdfs.userFilePath")
    public static String userFilePath;

    @Value("${hadoop.home")
    private String hadoopHome;

    @Value("${hadoop.user}")
    private String hadoopUser;

    @Bean
    public org.apache.hadoop.conf.Configuration hadoopConfig() {
        System.setProperty("hadoop.home.dir", hadoopHome);
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        configuration.set("fs.defaultFS", uri);
        configuration.set("dfs.permissions", "false");
        configuration.set("hadoop.job.ugi", hadoopUser);
        return configuration;
    }

    @Bean
    public FileSystem fileSystem() throws IOException {
        return FileSystem.get(hadoopConfig());
    }

}
