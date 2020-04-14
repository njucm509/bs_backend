package cn.edu.njucm.wp.bs.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Table(name = "bs_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4,max = 15,message = "用户名只能在4～15位之间")
    private String username;

    @JsonIgnore
    private String password;

    @Pattern(regexp = "^1[35678]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private byte flag;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
