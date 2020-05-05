package cn.edu.njucm.wp.bs.user.service.impl;

import cn.edu.njucm.wp.bs.common.encrypt.MD5Util;
import cn.edu.njucm.wp.bs.common.pojo.PageParam;
import cn.edu.njucm.wp.bs.common.pojo.PageResult;
import cn.edu.njucm.wp.bs.user.client.AuthClient;
import cn.edu.njucm.wp.bs.user.mapper.UserMapper;
import cn.edu.njucm.wp.bs.user.pojo.User;
import cn.edu.njucm.wp.bs.user.service.UserService;
import cn.edu.njucm.wp.bs.user.vo.UserVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    AuthClient authClient;

    @Override
    public User queryUser(String username, String password) {
        User record = new User();
        record.setName(username);
        return userMapper.selectOne(record);
    }

    @Override
    public Integer create(User user) {
        if (user.getPassword() != null) {
            user.setPassword(MD5Util.encrypt(user.getPassword()));
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
        }
        return userMapper.insertSelective(user);
    }

    @Override
    public User getByName(String name) {
        User record = new User();
        record.setName(name);
        return userMapper.selectOne(record);
    }

    @Override
    public Boolean bindRole(Long userId, List<Integer> roleId) {
        return authClient.bindRole(userId, roleId);
    }

    @Override
    public List<Integer> getRoleIdByUserId(Long id) {
        return authClient.getRoleByUserId(id);
    }

    @Override
    public Integer update(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public PageResult<UserVO> getUserByPageAndSort(PageParam param) {
        PageHelper.startPage(param.getPage(), param.getRows());
        Example example = new Example(User.class);
        if (StringUtils.isNotBlank(param.getKey())) {
            example.createCriteria().andLike("nickname", "%" + param.getKey() + "%");
        }
        if (StringUtils.isNotBlank(param.getSortBy())) {
            example.setOrderByClause(param.getSortBy() + (param.getDesc() ? " DESC" : " ASC"));
        }
        Page<User> users = (Page<User>) userMapper.selectByExample(example);
        Page<UserVO> page = new Page<>();
        BeanUtils.copyProperties(users, page);
        for (User user : users) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            List<Integer> roleId = getRoleIdByUserId(user.getId());
            userVO.setRoleId(roleId);
            page.add(userVO);
        }

        return new PageResult<>(page.getTotal(), page);
    }

    @Override
    public Boolean checkFlag(User user) {
        if (user.getFlag() == 0) {
            user.setFlag(1);
        }
        user.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));

        return userMapper.updateByPrimaryKeySelective(user) == 1;
    }

    @Override
    public void multi(MultipartFile file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        br.readLine();
        List<User> list = new ArrayList<>();
        String line = null;
        while ((line = br.readLine()) != null) {
            User user = new User();
            String[] split = line.split(",");
            user.setName(genereteAccount(8));
            if (split[0] != null) {
                user.setNickname(split[0]);
            }
            if (split[1] != null) {
                user.setPhone(split[1]);
            }
            if (split[2] != null) {
                user.setEmail(split[2]);
            }
            user.setFlag(0);
            user.setPassword("12345678");
            user.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            user.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
            list.add(user);
        }
        userMapper.insertList(list);
    }

    private String genereteAccount(int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
