package cn.edu.njucm.wp.bs.auth.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "fields")
public class Field {
    private Long id;
    private String sysName;
    private String name;
    private byte status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
