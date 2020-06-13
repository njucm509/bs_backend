package cn.edu.njucm.wp.bs.data.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "file_arg")
public class FileArg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "aes_key")
    private String aesKey;

    @Column(name = "rsa_key")
    private String rsaKey;

    @Column(name = "he_arg")
    private String heArg;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;
}
