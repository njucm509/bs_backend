package cn.edu.njucm.wp.bs.data.vo;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReleasedVO {

    private Integer id;

    private UserVO user;

    private Long handleFormId;

    private List<Field> usedFields;

    private String originData;

    private String releasedData;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
