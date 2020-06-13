package cn.edu.njucm.wp.bs.release.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Field {

    private Long id;

    private String sysName;

    private String name;

    private String type;

    private String descriptions;

    private int typeId;

    private int status;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
