package com.xts.service;

import com.xts.pojo.PageBean;
import com.xts.pojo.ShoppingCar;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;

public interface ShoppingCarService {
    /**
     * 查询全部购物车数据 分页
     * @return
     */
    PageBean pageSelect(String shopName, Integer userId,LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    /**
     * 根据id查询购物车数据
     * @param id
     * @return
     */
    ShoppingCar selectById(Integer id);

    /**
     * 添加购物车信息
     * @param shoppingCar
     */
    void insert(ShoppingCar shoppingCar);

    /**
     * 根据id删除购物车
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 修改购物车数据
     * @param shoppingCar
     */
    void update(ShoppingCar shoppingCar);
}
