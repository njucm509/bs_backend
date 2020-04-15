package cn.edu.njucm.wp.bs.auth.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "roles")
public class Role {
    private Long id;
    private String slug;
    private String name;
    private String descriptions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
