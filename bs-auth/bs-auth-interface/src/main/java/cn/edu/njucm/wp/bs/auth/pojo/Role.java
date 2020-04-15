package cn.edu.njucm.wp.bs.auth.pojo;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "roles")
public class Role {
    private Long id;
}
