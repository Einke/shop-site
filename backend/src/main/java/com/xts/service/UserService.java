package com.xts.service;

import com.xts.pojo.PageBean;
import com.xts.pojo.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    /**
     * 查询全部用户数据
     * @return
     */
    PageBean pageSelect(String username, Integer page, Integer pageSize);

    /**
     * 根据id查询用户数据
     * @param id
     * @return
     */
    User selectById(Integer id);

    /**
     * 添加用户信息
     * @param user
     */
    void insert(User user);

    /**
     * 根据id删除用户
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 修改用户数据
     * @param user
     */
    void update(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    User login(User user);
}
