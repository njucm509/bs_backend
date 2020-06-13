package cn.edu.njucm.wp.bs.data.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "released")
public class Released {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "handle_form_id")
    private Long handleFormId;

    @Column(name = "used_fields")
    private String usedFields;

    @Column(name = "origin_data")
    private String originData;

    @Column(name = "released_data")
    private String releasedData;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
