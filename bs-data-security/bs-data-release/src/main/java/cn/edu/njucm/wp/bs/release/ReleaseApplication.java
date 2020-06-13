package cn.edu.njucm.wp.bs.release;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("cn.edu.njucm.wp.bs.release.mapper")
public class ReleaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReleaseApplication.class, args);
    }
}
