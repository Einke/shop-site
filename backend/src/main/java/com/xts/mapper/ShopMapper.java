package com.xts.mapper;

import com.xts.pojo.Shop;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ShopMapper {
    /**
     * 查询全部商品数据
     * 条件查询分页展示
     * @return
     */
    List<Shop> list(String shopName,String category,Integer minPrice,Integer maxPrice,String sort);

    /**
     * 根据id查询商品数据
     * @param id
     * @return
     */
    Shop selectById(Integer id);

    /**
     * 新增商品信息
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
