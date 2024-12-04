package com.xts.mapper;

import com.xts.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 查询全部用户数据
     * 条件查询分页展示
     * @return
     */
    List<User> list(String username);

    /**
     * 根据id查询用户数据
     * @param id
     * @return
     */
    User selectById(Integer id);

    /**
     * 新增用户信息
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
    User getByUsernameAndPassword(User user);
}
