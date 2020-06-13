package cn.edu.njucm.wp.bs.release.service;

import cn.edu.njucm.wp.bs.release.pojo.HandleFormBean;
import cn.edu.njucm.wp.bs.release.vo.HandleFormBeanVO;

public interface HandleFormBeanService {
    Long create(HandleFormBean handleFormBean);

    Integer update(Long id);

    HandleFormBeanVO getHandleFormById(Long id);
}
