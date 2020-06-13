package cn.edu.njucm.wp.bs.data.pojo;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "upload_data_record")
public class UploadDataRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "upload_date")
    private String uploadDate;

    @Column(name = "upload_lines")
    private String uploadLines;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
