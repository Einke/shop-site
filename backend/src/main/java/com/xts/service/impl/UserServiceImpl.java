package com.xts.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xts.mapper.UserMapper;
import com.xts.pojo.PageBean;
import com.xts.pojo.User;
import com.xts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询全部用户数据 条件查询 分页展示
     * 用户名、创建时间
     * @return
     */
    @Override
    public PageBean pageSelect(String username, Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);

        //2.执行查询
        List<User> userList = userMapper.list(username);
        Page<User> p = (Page<User>) userList;

        //3.封装
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

    /**
     * 根据id查询用户数据
     * @param id
     * @return
     */
    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    /**
     * 新增用户信息
     * @param user
     */
    @Override
    public void insert(User user) {
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    /**
     * 根据id删除用户
     * @param ids
     */
    @Override
    public void delete(List<Integer> ids) {
        userMapper.delete(ids);
    }

    /**
     * 修改用户数据
     * @param user
     */
    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    /**
     * 登录
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userMapper.getByUsernameAndPassword(user);
    }
}
