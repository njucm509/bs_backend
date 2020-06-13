package cn.edu.njucm.wp.bs.encrypt.service;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.pojo.EncAlg;
import cn.edu.njucm.wp.bs.encrypt.mapper.EncAlgMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Slf4j
@Service
public class EncAlgService {

    @Autowired
    private EncAlgMapper mapper;

    public PageResult<EncAlg> queryUserByPageAndSort(PageParam pageParam) {
        PageHelper.startPage(pageParam.getPage(), pageParam.getRows());
        Example example = new Example(EncAlg.class);
        if (StringUtils.isNotBlank(pageParam.getKey())) {
//            example.createCriteria().andLike("name", "%" + pageParam.getKey() + "%");
        }
        if (StringUtils.isNotBlank(pageParam.getSortBy())) {
            String orderByClause = pageParam.getSortBy() + (pageParam.getDesc() ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        Page<EncAlg> pageInfo = (Page<EncAlg>) mapper.selectByExample(example);
        log.info("pageInfo: {}", pageInfo);
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    public void updateStatus(EncAlg encAlg) {
        int i = mapper.updateByPrimaryKeySelective(encAlg);
    }

    public List<EncAlg> getEncAlgList() {
        return mapper.selectAll();
    }
}
