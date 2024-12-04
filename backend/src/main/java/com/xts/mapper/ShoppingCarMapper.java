package com.xts.mapper;

import com.xts.pojo.ShoppingCar;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ShoppingCarMapper {
    /**
     * 查询全部购物车数据
     * 分页展示
     * @return
     */
    List<ShoppingCar> list(String shopName, Integer userId,LocalDate begin, LocalDate end);

    /**
     * 根据id查询购物车数据
     * @param id
     * @return
     */
    ShoppingCar selectById(Integer id);

    /**
     * 新增购物车信息
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
