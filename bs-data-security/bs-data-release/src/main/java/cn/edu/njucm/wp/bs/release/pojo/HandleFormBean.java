package cn.edu.njucm.wp.bs.release.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "handle_form")
public class HandleFormBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "used_fields")
    private String usedFields;

    @Column(name = "age_level")
    private Integer ageLevel;

    private Float sim;

    private Character mode;

    private Integer k;

    @Column(name = "user_id")
    private Long userId;

    private Integer status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
