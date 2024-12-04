package com.xts.service;

import com.xts.pojo.PageBean;
import com.xts.pojo.Orders;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;

public interface OrdersService {
    /**
     * 查询全部订单数据
     * @return
     */
    PageBean pageSelect(String shopName,Integer state, Integer userId,LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    /**
     * 根据id查询订单数据
     * @param id
     * @return
     */
    Orders selectById(Integer id);

    /**
     * 添加订单信息
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据id删除订单
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 修改订单数据
     * @param orders
     */
    void update(Orders orders);

    /**
     * 更新订单状态为2
     * @param id
     */
    void updateState2(Integer id);

    /**
     * 更新订单状态为1
     * @param id
     */
    void updateState1(Integer id);

    /**
     * 管理员的搜索
     * @param username
     * @param state
     * @param page
     * @param pageSize
     * @return
     */
    PageBean adminPageSelect(String username, Integer state, Integer page, Integer pageSize);



}
