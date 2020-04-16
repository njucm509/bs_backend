package cn.edu.njucm.wp.bs.auth.properties;

import cn.edu.njucm.wp.bs.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Slf4j
@RefreshScope
@Configuration
public class JwtProperties {
    @Value("${bs.jwt.secret}")
    private String secret;
    @Value("${bs.jwt.pubKeyPath}")
    private String pubKeyPath;
    @Value("${bs.jwt.priKeyPath}")
    private String priKeyPath;
    @Value("${bs.jwt.expire}")
    private int expire;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    @Value("${bs.jwt.cookieName}")
    private String cookieName;
    @Value("${bs.jwt.cookieMaxAge}")
    private Integer cookieMaxAge;

    @PostConstruct
    public void init() {
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            log.info(pubKey.getAbsolutePath());
            log.info(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
            privateKey = RsaUtils.getPrivateKey(priKeyPath);
            log.info("init key success..");
        } catch (Exception e) {
            log.error("初始化公钥和私钥失败!");
            throw new RuntimeException();
        }
    }
}
