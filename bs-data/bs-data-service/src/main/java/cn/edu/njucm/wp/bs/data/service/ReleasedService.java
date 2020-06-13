package cn.edu.njucm.wp.bs.data.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.Released;
import cn.edu.njucm.wp.bs.data.vo.ReleasedVO;

import java.util.List;

public interface ReleasedService {
    PageResult<ReleasedVO> getReleasedByPage(PageParam param);

    List<ReleasedVO> getReleased();

    Integer create(Released released);

    Boolean check(Released released);

    ReleasedVO getReleasedById(Integer id);
}
