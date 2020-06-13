package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.client.AuthClient;
import cn.edu.njucm.wp.bs.data.mapper.FieldMapper;
import cn.edu.njucm.wp.bs.data.mapper.TypeMapper;
import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.Type;
import cn.edu.njucm.wp.bs.data.service.FieldService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class FieldServiceImpl implements FieldService {

    @Autowired
    private FieldMapper fieldMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private AuthClient authClient;

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
        Map<String, Object> check = fieldMapper.check(field);
        int count = 0;
        if (check.containsKey("count")) {
            count = Integer.parseInt(String.valueOf(check.get("count")));
        }
        return count == 1;
    }

    @Override
    public List<Field> list() {
        return fieldMapper.selectAll();
    }

    @Override
    public PageResult<Field> getFieldByPageAndSort(PageParam param) {
        Example example = new Example(Field.class);

        Example.Criteria criteria = example.createCriteria();

        if (param.getUserId() != null) {
            List<Integer> roleIds = authClient.getRoleByUserId(param.getUserId());
            List<Integer> ids = fieldMapper.getFieldIdByRoleIds(roleIds);
            criteria.andIn("id", ids);
        }

        if (StringUtils.isNotBlank(param.getKey())) {
            criteria.andLike("name", "%" + param.getKey() + "%");
        }

        if (StringUtils.isNotBlank(param.getSortBy())) {
            example.setOrderByClause(param.getSortBy() + (param.getDesc() ? " DESC" : " ASC"));
        }

        PageHelper.startPage(param.getPage(), param.getRows());
        Page<Field> page = (Page<Field>) fieldMapper.selectByExample(example);

        return new PageResult<>(page.getTotal(), page);
    }

    @Override
    public List<Field> getFieldByUserId(Long id) {
        List<Integer> roleIds = authClient.getRoleByUserId(id);
        List<Integer> ids = fieldMapper.getFieldIdByRoleIds(roleIds);
        return fieldMapper.getFieldByIds(ids);
    }

    @Override
    public List<Field> getAnomyousFieldByUserId(Long id, String date) {
        return getFieldByUserId(id);
    }

    @Override
    public void dropInitTable(List<String> list) {
        for (String name : list) {
            fieldMapper.drop(name);
        }
    }

    @Override
    public List<Field> getFieldByIds(String ids) {
        String[] ss = ids.split(",");
        List<Integer> list = new ArrayList<>();
        for (String s : ss) {
            if (StringUtils.isNumeric(s)) {
                list.add(Integer.parseInt(s));
            }
        }

        return fieldMapper.getFieldByIds(list);
    }

    @Override
    public Boolean init(String tableName, int type) {
        List<Field> fields = fieldMapper.selectAll();
        List<String> cols = new ArrayList<>();
        String id = "`id` bigint auto_increment primary key";
        cols.add(id);
        String userId = "`user_id` bigint(20)";
        cols.add(userId);
        String date = "`date` varchar(50)";
        cols.add(date);
        for (Field field : fields) {
            String col;
            if (type == 1) {
                if (field.getSysName().toUpperCase().equals("AGE")) {
                    col = "`" + field.getSysName().toUpperCase() + "` varchar(20)";
                } else if (field.getSysName().toUpperCase().equals("SEX")) {
                    col = "`" + field.getSysName().toUpperCase() + "` varchar(10)";
                } else {
                    col = "`" + field.getSysName().toUpperCase() + "` " + field.getType();
                }
            } else {
                col = "`" + field.getSysName().toUpperCase() + "` " + field.getType();
            }

            cols.add(col);
        }

        fieldMapper.createTable(tableName, cols);

        return fieldMapper.isExist(tableName) > 0;
    }

    @Override
    public Boolean checkTable(String tableName) {
        return fieldMapper.isExist(tableName) > 0;
    }

    @Override
    public Boolean updateTableScheme() {
        return fieldMapper.updateTableScheme() > 0;
    }

    @Override
    public List<Type> getTypeList() {
        return typeMapper.selectAll();
    }

    @Override
    public Boolean update(Field field) {
        return fieldMapper.updateByPrimaryKeySelective(field) == 1;
    }

}
