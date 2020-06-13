package cn.edu.njucm.wp.bs.encrypt.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spark 配置
 *
 * @author wp
 */
@Slf4j
@Configuration
public class SparkConfig {

    @Value("${spark.app.name}")
    private String appName;

    @Value("${spark.home}")
    private String sparkHome;

    @Value("${spark.master.uri}")
    private String masterUri;

    @Bean
    public SparkConf sparkConf() {
        SparkConf sparkConf = new SparkConf()
                .setAppName(appName)
                .setSparkHome(sparkHome)
                .setMaster(masterUri);
        log.info("local spark init {}--{}--{}", sparkHome, masterUri, appName);
        return sparkConf;
    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        log.info("local java spark context init...");
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession
                .builder()
                .sparkContext(javaSparkContext().sc())
                .appName("java spark sql")
                .getOrCreate();
    }
}
