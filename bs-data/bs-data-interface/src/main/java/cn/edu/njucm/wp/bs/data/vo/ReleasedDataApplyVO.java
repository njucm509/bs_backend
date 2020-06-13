package cn.edu.njucm.wp.bs.data.vo;

import cn.edu.njucm.wp.bs.user.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReleasedDataApplyVO {
    private Long id;

    private ReleasedVO released;

    private UserVO applyUser;

    private UserVO handleUser;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
