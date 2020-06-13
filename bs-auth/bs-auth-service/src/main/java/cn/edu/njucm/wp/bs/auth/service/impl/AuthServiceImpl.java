package cn.edu.njucm.wp.bs.auth.service.impl;

import cn.edu.njucm.wp.bs.auth.bo.UserInfo;
import cn.edu.njucm.wp.bs.auth.client.UserClient;
import cn.edu.njucm.wp.bs.auth.mapper.PermissionMapper;
import cn.edu.njucm.wp.bs.auth.mapper.RoleMapper;
import cn.edu.njucm.wp.bs.auth.pojo.Permission;
import cn.edu.njucm.wp.bs.auth.pojo.Role;
import cn.edu.njucm.wp.bs.auth.properties.JwtProperties;
import cn.edu.njucm.wp.bs.auth.service.AuthService;
import cn.edu.njucm.wp.bs.auth.utils.JwtUtils;
import cn.edu.njucm.wp.bs.auth.vo.RoleVO;
import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.user.pojo.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    UserClient userClient;

    @Autowired
    JwtProperties properties;

    @Override
    @Transactional
    public Integer create(Role role) {
        if (role.getCreatedAt() == null) {
            role.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        if (role.getUpdatedAt() == null) {
            role.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        return roleMapper.insert(role);
    }

    @Override
    public Integer delete(Integer id) {
        Role role = new Role();
        role.setId(id);
        return roleMapper.delete(role);
    }

    @Override
    public List<Role> list() {
        return roleMapper.selectAll();
    }

    @Override
    @Transactional
    public Integer update(Role role) {
        role.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        RoleVO roleVO = (RoleVO) role;
        if (roleVO.getPermissionId() != null) {
            roleMapper.deleteRolePermission(roleVO.getId());
            for (Integer id : roleVO.getPermissionId()) {
                permissionMapper.bindPermission(roleVO.getId(), id);
            }
        }

        if (roleVO.getFieldId() != null) {
            roleMapper.deleteRoleField(roleVO.getId());
            for (Integer id : roleVO.getFieldId()) {
                roleMapper.bindField(roleVO.getId(), id);
            }
        }

        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public Boolean check(Role role) {
        Map<String, Object> check = roleMapper.check(role);
        int count = 0;
        if (check.containsKey("count")) {
            count = Integer.parseInt(String.valueOf(check.get("count")));
        }
        return count == 1;
    }

    @Override
    public List<Integer> getRoleIdByUserId(Long id) {
        List<Integer> roleIds = roleMapper.getRoleIdByUserId(id);
        System.out.println(id);
        Queue<Integer> roles = new LinkedList<>();
        List<Integer> res = new ArrayList<>(roleIds);
        System.out.println(roleIds);
        for (Integer roleId : roleIds) {
            Role role = roleMapper.selectByPrimaryKey(roleId);
            roles.add(role.getParentId());
            while (!roles.isEmpty()) {
                Integer parentId = roles.poll();
                if (!parentId.equals(0)) {
                    Role parentRole = roleMapper.selectByPrimaryKey(parentId);
                    res.add(parentId);
                    roles.add(parentRole.getParentId());
                }
            }
        }
        System.out.println(res);
        return res;
    }

    @Override
    public Role getRoleByUserId(Long id) {
        roleMapper.getRoleByUserId(id);
        return null;
    }

    @Override
    public String authentication(String username, String password) {
        try {
            User user = userClient.queryUser(username, password);
            if (user == null) {
                return null;
            }
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getName()), properties.getPrivateKey(), properties.getExpire());
            log.info("token generate: {}", token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer bindRole(Long userId, List<Integer> roleId) {

        Integer res = 0;
        for (Integer id : roleId) {
            res += roleMapper.bindRole(userId, id);
        }
        return res;
    }

    @Override
    public List<Permission> permissionList() {
        return permissionMapper.selectAll();
    }

    @Override
    public List<Integer> getPermissionIdByRoleId(List<Integer> ids) {
        return permissionMapper.getPermissionIdByRoleId(ids);
    }

    public List<Integer> getPermissionIdByRoleId(Integer id) {
        return permissionMapper.getPermissionIdsByRoleId(id);
    }

    @Override
    public Boolean check(Permission permission) {
        return permissionMapper.check(permission) == 1 ? true : false;
    }

    @Override
    public Boolean create(Permission permission) {
        if (permission.getCreatedAt() == null) {
            permission.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }

        if (permission.getUpdatedAt() == null) {
            permission.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        return permissionMapper.insert(permission) == 1;
    }

    @Override
    public Boolean update(Permission permission) {
        permission.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        return permissionMapper.updateByPrimaryKeySelective(permission) == 1;
    }

    @Override
    public PageResult<RoleVO> list(PageParam param) {
        PageHelper.startPage(param.getPage(), param.getRows());
        Example example = new Example(Role.class);
        if (StringUtils.isNotBlank(param.getKey())) {
            example.createCriteria().andLike("name", "%" + param.getKey() + "%");
        }
        if (StringUtils.isNotBlank(param.getSortBy())) {
            example.setOrderByClause(param.getSortBy() + (param.getDesc() ? " DESC" : " ASC"));
        }
        Page<Role> roles = (Page<Role>) roleMapper.selectByExample(example);
        Page<RoleVO> page = new Page<>();
        BeanUtils.copyProperties(roles, page);
        for (Role role : roles) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            List<Integer> permissionId = getPermissionIdByRoleId(role.getId());
            List<Integer> fieldId = getFieldIdByRoleId(role.getId());
            roleVO.setPermissionId(permissionId);
            roleVO.setFieldId(fieldId);
            page.add(roleVO);
        }

        return new PageResult<>(page.getTotal(), page);
    }

    public List<Integer> getFieldIdByRoleId(Integer id) {
        return roleMapper.getFieldIdByRoleId(id);
    }


    @Override
    public Integer bindPermission(Integer roleId, List<Integer> permissionId) {
        Integer res = 0;
        for (Integer id : permissionId) {
            res += permissionMapper.bindPermission(roleId, id);
        }
        return res;
    }

    @Override
    public Integer bindField(Integer roleId, List<Integer> fieldId) {
        Integer res = 0;
        for (Integer id : fieldId) {
            res += roleMapper.bindField(roleId, id);
        }
        return res;
    }

    @Override
    public List<Role> getRootRole() {
        return roleMapper.getRootRole();
    }

}
