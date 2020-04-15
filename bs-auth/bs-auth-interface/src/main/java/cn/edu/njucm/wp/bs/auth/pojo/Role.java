package cn.edu.njucm.wp.bs.auth.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "roles")
public class Role {
    private Long id;

    private String slug;

    private String name;

    private String descriptions;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
