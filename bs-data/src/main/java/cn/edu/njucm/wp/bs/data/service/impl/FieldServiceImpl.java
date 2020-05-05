package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.mapper.FieldMapper;
import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.service.FieldService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldMapper fieldMapper;

    @Override
    public Boolean create(Field field) {
        if (field.getCreatedAt() == null) {
            field.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        if (field.getUpdatedAt() == null) {
            field.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        return fieldMapper.insertSelective(field) == 1;
    }

    @Override
    public Boolean check(Field field) {
        return fieldMapper.check(field) == 1;
    }

    @Override
    public List<Field> list() {
        return fieldMapper.selectAll();
    }

    @Override
    public PageResult<Field> getFieldByPageAndSort(PageParam param) {
        PageHelper.startPage(param.getPage(), param.getRows());
        Example example = new Example(Field.class);
        if (StringUtils.isNotBlank(param.getKey())) {
            example.createCriteria().andLike("name", "%" + param.getKey() + "%");
        }
        if (StringUtils.isNotBlank(param.getSortBy())) {
            example.setOrderByClause(param.getSortBy() + (param.getDesc() ? " DESC" : " ASC"));
        }
        Page<Field> page = (Page<Field>) fieldMapper.selectByExample(example);

        return new PageResult<>(page.getTotal(), page);
    }
}
