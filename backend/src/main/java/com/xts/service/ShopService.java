package com.xts.service;

import com.xts.pojo.PageBean;
import com.xts.pojo.Shop;

import java.time.LocalDate;
import java.util.List;

public interface ShopService {
    /**
     * 查询全部商品数据
     * @return
     */
    PageBean pageSelect(String shopName,String category,Integer minPrice,Integer maxPrice, String sort,Integer page, Integer pageSize);

    /**
     * 根据id查询商品数据
     * @param id
     * @return
     */
    Shop selectById(Integer id);

    /**
     * 添加商品信息
     * @param shop
     */
    void insert(Shop shop);

    /**
     * 根据id删除商品
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 修改商品数据
     * @param shop
     */
    void update(Shop shop);
}
