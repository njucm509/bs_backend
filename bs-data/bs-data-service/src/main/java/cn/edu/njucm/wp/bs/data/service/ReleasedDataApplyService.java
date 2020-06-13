package cn.edu.njucm.wp.bs.data.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.vo.ReleasedDataApplyHandleBean;
import cn.edu.njucm.wp.bs.data.vo.ReleasedDataApplyVO;

public interface ReleasedDataApplyService {
    PageResult<ReleasedDataApplyVO> getByPage(PageParam param);

    Boolean create(ReleasedDataApplyHandleBean releasedDataApplyHandleBean);

    Boolean check(ReleasedDataApplyHandleBean releasedDataApplyHandleBean);

    Boolean update(Long id);
}
