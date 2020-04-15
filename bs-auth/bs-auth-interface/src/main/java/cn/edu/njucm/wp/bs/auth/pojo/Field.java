package cn.edu.njucm.wp.bs.auth.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "fields")
public class Field {
    private Long id;

    private String sysName;

    private String name;

    private byte status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
