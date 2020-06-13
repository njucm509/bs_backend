package cn.edu.njucm.wp.bs.encrypt.entity;

import lombok.Data;

import java.util.Date;

@Data
public class HbaseRow {
    private String rowKey;
    private String columnFamily;
    private String column;
    private String value;
    private Long time;
}
