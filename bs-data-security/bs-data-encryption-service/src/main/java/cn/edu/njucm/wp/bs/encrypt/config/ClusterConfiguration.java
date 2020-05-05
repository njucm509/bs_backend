package cn.edu.njucm.wp.bs.encrypt.config;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Configuration
public class ClusterConfiguration {
    @Value("${cluster.master}")
    private String master;
    @Value("${cluster.master2}")
    private String master2;
    @Value("${cluster.slave1}")
    private String slave1;
    @Value("${cluster.slave2}")
    private String slave2;
    @Value("${cluster.slave3}")
    private String slave3;
    @Value("${cluster.login.name}")
    private String loginName;
    @Value("${cluster.login.password}")
    private String password;

    public Session getSession(String host, String name, String password) {
        Session session = null;
        if (host != null) {

            Connection connection = new Connection(host, 22);
            log.info("init host:{} connect... {}", host, connection);
            try {
                connection.connect();
                boolean b = connection.authenticateWithPassword(name, password);
                if (!b) {
                    log.error("验证失败!");
                } else {
                    session = connection.openSession();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return session;
    }

    public Connection getConnection(String host, String name, String password) {
        Connection connection = null;
        if (host != null) {
            connection = new Connection(host, 22);
            log.info("init host:{} connect... {}", host, connection);
            try {
                connection.connect();
                boolean b = connection.authenticateWithPassword(name, password);
                if (!b) {
                    log.error("验证失败！");
                    return connection;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * ssh 连接池
     *
     * @return
     */
    @Bean
    public HashMap<String, Connection> connections() {
        return new HashMap<String, Connection>() {
            {
                this.put("master", getConnection(master, loginName, password));
                this.put("master2", getConnection(master2, loginName, password));
                this.put("slave1", getConnection(slave1, loginName, password));
                this.put("slave2", getConnection(slave2, loginName, password));
                this.put("slave3", getConnection(slave3, loginName, password));
            }
        };
    }
}
