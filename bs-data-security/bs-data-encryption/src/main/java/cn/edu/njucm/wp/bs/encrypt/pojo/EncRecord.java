package cn.edu.njucm.wp.bs.encrypt.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "enc_record")
public class EncRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Long userId;

    private String filename;

    private String record;

    @Column(name = "create_time")
    private Date createAt;

}
