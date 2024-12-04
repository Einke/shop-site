package com.xts.mapper;

import com.xts.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrdersMapper {
    /**
     * 查询全部订单数据
     * 条件查询分页展示
     * @return
     */
    List<Orders> list(String shopName,Integer userId, Integer state, LocalDate begin, LocalDate end);

    /**
     * 根据id查询订单数据
     * @param id
     * @return
     */
    Orders selectById(Integer id);

    /**
     * 新增订单信息
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
     * 更新订单状态为已发货
     * @param id
     */
    void updateState2(Integer id);

     /**
     * 更改订单状态为1
     * @param id
     */
    void updateState1(Integer id);

    /**
     * 管理员查询订单
     * @param username
     * @param state
     * @return
     */
    List<Orders> adminList(String username, Integer state);


}
