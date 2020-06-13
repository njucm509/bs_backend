package cn.edu.njucm.wp.bs.data.vo;

import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReleasedDataApplyHandleBean {

    // released_id
    private Integer id;

    private List<Long> applyUserId;

    // handle_user_id
    private UserVO user;

    private Long handleFormId;

    private String originData;

    private String releasedData;

    private List<FieldVO> usedFields;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
