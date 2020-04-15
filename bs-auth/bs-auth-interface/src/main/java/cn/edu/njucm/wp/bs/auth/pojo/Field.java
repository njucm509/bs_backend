package cn.edu.njucm.wp.bs.auth.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "fields")
public class Field {
    private Long id;
    private String sysName;
    private String name;
    private byte status;
}
