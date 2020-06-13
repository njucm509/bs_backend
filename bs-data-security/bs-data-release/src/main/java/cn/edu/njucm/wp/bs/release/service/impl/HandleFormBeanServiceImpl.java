package cn.edu.njucm.wp.bs.release.service.impl;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.release.client.DataClient;
import cn.edu.njucm.wp.bs.release.client.UserClient;
import cn.edu.njucm.wp.bs.release.mapper.HandleFormBeanMapper;
import cn.edu.njucm.wp.bs.release.pojo.HandleFormBean;
import cn.edu.njucm.wp.bs.release.service.HandleFormBeanService;
import cn.edu.njucm.wp.bs.release.vo.HandleFormBeanVO;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class HandleFormBeanServiceImpl implements HandleFormBeanService {

    @Autowired
    HandleFormBeanMapper handleFormBeanMapper;

    @Autowired
    UserClient userClient;

    @Autowired
    DataClient dataClient;

    @Override
    public Long create(HandleFormBean handleFormBean) {

        if (handleFormBean.getCreatedAt() == null) {
            handleFormBean.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        if (handleFormBean.getUpdatedAt() == null) {
            handleFormBean.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        handleFormBeanMapper.insertSelective(handleFormBean);
        return handleFormBean.getId();
    }

    @Override
    public Integer update(Long id) {
        HandleFormBean bean = new HandleFormBean();
        bean.setId(id);
        bean.setStatus(1);
        bean.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        return handleFormBeanMapper.updateByPrimaryKeySelective(bean);
    }

    @Override
    public HandleFormBeanVO getHandleFormById(Long id) {
        HandleFormBean bean = handleFormBeanMapper.selectByPrimaryKey(id);
        HandleFormBeanVO beanVO = new HandleFormBeanVO();
        BeanUtils.copyProperties(bean, beanVO);

        UserVO user = userClient.getUserById(bean.getUserId());
        beanVO.setUser(user);

        List<Field> fields = dataClient.getByIds(bean.getUsedFields());
        beanVO.setUsedFields(fields);

        return beanVO;
    }
}
