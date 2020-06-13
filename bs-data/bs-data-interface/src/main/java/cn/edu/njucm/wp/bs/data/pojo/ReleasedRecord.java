package cn.edu.njucm.wp.bs.data.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "released_record")
public class ReleasedRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "released_id")
    private Integer releasedId;

    @Column(name = "released_data_apply_id")
    private Long releasedDataApplyId;

    @Column(name = "apply_user_id")
    private Long applyUserId;

    @Column(name = "handle_user_id")
    private Long handleUserId;

    @Column(name = "used_fields")
    private String usedFields;

    @Column(name = "can_read")
    private String canRead;

    @Column(name = "can_write")
    private String canWrite;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
