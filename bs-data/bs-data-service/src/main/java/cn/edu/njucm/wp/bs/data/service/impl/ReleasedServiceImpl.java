package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.client.AuthClient;
import cn.edu.njucm.wp.bs.data.client.UserClient;
import cn.edu.njucm.wp.bs.data.mapper.FieldMapper;
import cn.edu.njucm.wp.bs.data.mapper.ReleasedMapper;
import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.Released;
import cn.edu.njucm.wp.bs.data.service.ReleasedService;
import cn.edu.njucm.wp.bs.data.vo.ReleasedVO;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ReleasedServiceImpl implements ReleasedService {

    @Autowired
    ReleasedMapper releasedMapper;

    @Autowired
    FieldMapper fieldMapper;

    @Autowired
    AuthClient authClient;

    @Autowired
    UserClient userClient;

    @Override
    public PageResult<ReleasedVO> getReleasedByPage(PageParam param) {
        Example example = new Example(Released.class);

        Example.Criteria criteria = example.createCriteria();

//        if (StringUtils.isNotBlank(param.getKey())) {
//            criteria.andLike("name", "%" + param.getKey() + "%");
//        }

        if (param.getUserId() != null) {
            List<Integer> ids = authClient.getRoleByUserId(param.getUserId());
            if (ids.contains(1)) {

            } else {
                criteria.andEqualTo("userId", param.getUserId());
            }
        }

        if (StringUtils.isNotBlank(param.getSortBy())) {
            example.setOrderByClause(param.getSortBy() + (param.getDesc() ? " DESC" : " ASC"));
        }

        PageHelper.startPage(param.getPage(), param.getRows());
        Page<Released> releaseds = (Page<Released>) releasedMapper.selectByExample(example);

        Page<ReleasedVO> page = new Page<>();
        BeanUtils.copyProperties(releaseds, page);
        for (Released released : releaseds) {
            ReleasedVO releasedVO = new ReleasedVO();
            BeanUtils.copyProperties(released, releasedVO);
            List<Field> fields = getFieldsByFieldIds(released.getUsedFields());
            releasedVO.setUsedFields(fields);
            UserVO user = userClient.getUserById(released.getUserId());
            releasedVO.setUser(user);

            page.add(releasedVO);
        }

        return new PageResult<>(page.getTotal(), page);
    }

    private List<Field> getFieldsByFieldIds(String fields) {
        String[] fieldIds = fields.split(",");
        List<Integer> list = new ArrayList<>();
        for (String id : fieldIds) {
            list.add(Integer.parseInt(id));
        }

        return fieldMapper.getFieldByIds(list);
    }

    @Override
    public List<ReleasedVO> getReleased() {
        List<Released> list = releasedMapper.selectAll();
        List<ReleasedVO> res = new ArrayList<>();
        for (Released released : list) {
            ReleasedVO releasedVO = new ReleasedVO();
            BeanUtils.copyProperties(released, releasedVO);
            List<Field> fields = getFieldsByFieldIds(released.getUsedFields());
            releasedVO.setUsedFields(fields);
            UserVO user = userClient.getUserById(released.getUserId());
            releasedVO.setUser(user);

            res.add(releasedVO);
        }

        return res;
    }

    @Override
    public Integer create(Released released) {
        if (released.getCreatedAt() == null) {
            released.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        if (released.getUpdatedAt() == null) {
            released.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        releasedMapper.insertSelective(released);
        return released.getId();
    }

    @Override
    public Boolean check(Released released) {
        HashMap<String, Object> res = releasedMapper.check(released);
        System.out.println(res);
        Long count = (Long) res.get("count");
        return count > 0;
    }

    @Override
    public ReleasedVO getReleasedById(Integer id) {
        Released released = releasedMapper.selectByPrimaryKey(id);
        ReleasedVO releasedVO = new ReleasedVO();

        BeanUtils.copyProperties(released, releasedVO);

        UserVO user = userClient.getUserById(released.getUserId());
        releasedVO.setUser(user);

        List<Field> fields = getFieldsByFieldIds(released.getUsedFields());
        releasedVO.setUsedFields(fields);

        return releasedVO;
    }
}
