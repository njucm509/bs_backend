package cn.edu.njucm.wp.bs.release.vo;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HandleFormBeanVO {
    private Long id;

    private List<Field> usedFields;

    private Integer ageLevel;

    private Float sim;

    private Character mode;

    private UserVO user;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
