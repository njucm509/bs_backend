package cn.edu.njucm.wp.bs.encrypt.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.encrypt.mapper.EncRecordMapper;
import cn.edu.njucm.wp.bs.encrypt.pojo.EncRecord;
import cn.edu.njucm.wp.bs.user.pojo.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;

@Slf4j
@Service
public class EncRecordService implements Serializable {

    @Autowired
    private EncRecordMapper mapper;

    public PageResult<EncRecord> queryUserByPageAndSort(PageParam pageParam) {
        PageHelper.startPage(pageParam.getPage(), pageParam.getRows());
        Example example = new Example(EncRecord.class);
        if (StringUtils.isNotBlank(pageParam.getKey())) {
//            example.createCriteria().andLike("name", "%" + pageParam.getKey() + "%");
        }
        if (StringUtils.isNotBlank(pageParam.getSortBy())) {
            String orderByClause = pageParam.getSortBy() + (pageParam.getDesc() ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        Page<EncRecord> pageInfo = (Page<EncRecord>) mapper.selectByExample(example);
        log.info("pageInfo: {}", pageInfo);
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    public void delete(EncRecord encRecord) {
        mapper.delete(encRecord);
    }

    public void create(EncRecord encRecord) {
        mapper.insertSelective(encRecord);
    }

    public PageResult<EncRecord> queryUserByPageAndSort(PageParam pageParam, User user) {

//        if (user.getRole() == 1) {
//            return queryUserByPageAndSort(pageParam);
//        } else {
//            PageHelper.startPage(pageParam.getPage(), pageParam.getRows());
//            EncRecord record = new EncRecord();
//            record.setUserId(user.getId());
//            Page<EncRecord> pageInfo = (Page<EncRecord>) mapper.select(record);
//            log.info("pageInfo: {}", pageInfo);
//            return new PageResult<>(pageInfo.getTotal(), pageInfo);
//        }

        return null;

    }
}
