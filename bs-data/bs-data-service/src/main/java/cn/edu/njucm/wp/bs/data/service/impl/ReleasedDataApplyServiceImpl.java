package cn.edu.njucm.wp.bs.data.service.impl;

import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.data.client.AuthClient;
import cn.edu.njucm.wp.bs.data.client.UserClient;
import cn.edu.njucm.wp.bs.data.mapper.ReleasedDataApplyMapper;
import cn.edu.njucm.wp.bs.data.mapper.ReleasedMapper;
import cn.edu.njucm.wp.bs.data.mapper.ReleasedRecordMapper;
import cn.edu.njucm.wp.bs.data.pojo.Released;
import cn.edu.njucm.wp.bs.data.pojo.ReleasedDataApply;
import cn.edu.njucm.wp.bs.data.pojo.ReleasedRecord;
import cn.edu.njucm.wp.bs.data.service.ReleasedDataApplyService;
import cn.edu.njucm.wp.bs.data.service.ReleasedService;
import cn.edu.njucm.wp.bs.data.vo.FieldVO;
import cn.edu.njucm.wp.bs.data.vo.ReleasedDataApplyHandleBean;
import cn.edu.njucm.wp.bs.data.vo.ReleasedDataApplyVO;
import cn.edu.njucm.wp.bs.data.vo.ReleasedVO;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReleasedDataApplyServiceImpl implements ReleasedDataApplyService {

    @Autowired
    ReleasedDataApplyMapper releasedDataApplyMapper;

    @Autowired
    ReleasedRecordMapper releasedRecordMapper;

    @Autowired
    ReleasedService releasedService;

    @Autowired
    UserClient userClient;

    @Autowired
    AuthClient authClient;

    @Override
    public PageResult<ReleasedDataApplyVO> getByPage(PageParam param) {
        Example example = new Example(ReleasedDataApply.class);

        if (StringUtils.isNotBlank(param.getSortBy())) {
            example.setOrderByClause(param.getSortBy() + (param.getDesc() ? " DESC" : " ASC"));
        }

        Example.Criteria criteria = example.createCriteria();

        if (param.getUserId() != null) {
            List<Integer> ids = authClient.getRoleByUserId(param.getUserId());
            if (ids.contains(1)) {

            } else {
                if (param.getApplyOrHandle().equals(0)) {
                    criteria.andEqualTo("applyUserId", param.getUserId());
                    if (param.getStatus() != null && param.getStatus().equals(1)) {
                        criteria.andEqualTo("status", 1);
                    }
                }
                if (param.getApplyOrHandle().equals(1)) {
                    criteria.andEqualTo("handleUserId", param.getUserId());
                }
            }
        }
        PageHelper.startPage(param.getPage(), param.getRows());

        Page<ReleasedDataApply> releasedDataApplies = (Page<ReleasedDataApply>) releasedDataApplyMapper.selectByExample(example);

        Page<ReleasedDataApplyVO> page = new Page<>();
        BeanUtils.copyProperties(releasedDataApplies, page);

        for (ReleasedDataApply apply : releasedDataApplies) {
            ReleasedDataApplyVO applyVO = new ReleasedDataApplyVO();
            BeanUtils.copyProperties(apply, applyVO);

            UserVO applyUser = userClient.getUserById(apply.getApplyUserId());
            UserVO handleUser = userClient.getUserById(apply.getHandleUserId());
            ReleasedVO released = releasedService.getReleasedById(apply.getReleasedId());
            applyVO.setApplyUser(applyUser);
            applyVO.setHandleUser(handleUser);
            applyVO.setReleased(released);

            page.add(applyVO);
        }

        return new PageResult<>(page.getTotal(), page);
    }

    @Override
    public Boolean create(ReleasedDataApplyHandleBean releasedDataApplyHandleBean) {
        int res = 0;
        for (Long id : releasedDataApplyHandleBean.getApplyUserId()) {
            ReleasedDataApply releasedDataApply = new ReleasedDataApply();
            releasedDataApply.setReleasedId(releasedDataApplyHandleBean.getId());
            releasedDataApply.setApplyUserId(id);
            releasedDataApply.setHandleUserId(releasedDataApplyHandleBean.getUser().getId());
            releasedDataApply.setStatus(0);
            releasedDataApply.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            releasedDataApply.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

            int i = releasedDataApplyMapper.insertUseGeneratedKeys(releasedDataApply);

            ReleasedRecord releasedRecord = new ReleasedRecord();

            releasedRecord.setReleasedId(releasedDataApplyHandleBean.getId());

            List<FieldVO> usedFields = releasedDataApplyHandleBean.getUsedFields();

            List<FieldVO> fieldsCanNotRead = usedFields.stream().filter(fieldVO -> fieldVO.getAuth().equals(0)).collect(Collectors.toList());
            List<FieldVO> fieldsCanRead = usedFields.stream().filter(fieldVO -> fieldVO.getAuth().equals(1)).collect(Collectors.toList());

            List<Long> allFields = usedFields.stream().map(FieldVO::getId).distinct().collect(Collectors.toList());
            StringBuilder all = new StringBuilder();
            for (Long field : allFields) {
                all.append(field).append(",");
            }
            releasedRecord.setUsedFields(all.toString());

            List<Long> canReadFields = fieldsCanRead.stream().map(FieldVO::getId).distinct().collect(Collectors.toList());
            StringBuilder canRead = new StringBuilder();
            for (Long field : canReadFields) {
                canRead.append(field).append(",");
            }
            releasedRecord.setCanRead(canRead.toString());

            releasedRecord.setReleasedDataApplyId(releasedDataApply.getId());

            releasedRecord.setApplyUserId(id);
            releasedRecord.setHandleUserId(releasedDataApplyHandleBean.getUser().getId());

            releasedRecord.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            releasedRecord.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

            int j = releasedRecordMapper.insertUseGeneratedKeys(releasedRecord);

            res += i + j;
        }


        return res > 0;
    }

    @Override
    public Boolean check(ReleasedDataApplyHandleBean releasedDataApplyHandleBean) {
        int res = 0;
        if (releasedDataApplyHandleBean.getApplyUserId().size() > 1) {
            for (Long id : releasedDataApplyHandleBean.getApplyUserId()) {
                ReleasedDataApply apply = new ReleasedDataApply();
                apply.setApplyUserId(id);
                apply.setHandleUserId(releasedDataApplyHandleBean.getUser().getId());
                apply.setReleasedId(releasedDataApplyHandleBean.getId());
                res += releasedDataApplyMapper.selectCount(apply);
            }
        } else {
            ReleasedDataApply apply = new ReleasedDataApply();
            apply.setApplyUserId(releasedDataApplyHandleBean.getApplyUserId().get(0));
            apply.setHandleUserId(releasedDataApplyHandleBean.getUser().getId());
            apply.setReleasedId(releasedDataApplyHandleBean.getId());
            res = releasedDataApplyMapper.selectCount(apply);
        }

        if (res > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Boolean update(Long id) {
        ReleasedDataApply releasedDataApply = new ReleasedDataApply();
        releasedDataApply.setId(id);
        releasedDataApply.setStatus(1);
        releasedDataApply.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        return releasedDataApplyMapper.updateByPrimaryKeySelective(releasedDataApply) == 1;
    }

}
