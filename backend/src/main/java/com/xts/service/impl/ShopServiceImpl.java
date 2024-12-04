package com.xts.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xts.mapper.ShopMapper;
import com.xts.pojo.PageBean;
import com.xts.pojo.Shop;
import com.xts.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    /**
     * 根据条件查询商品信息，分页展示
     * 商品名，商品分类，商品价格范围，创建时间
     * @param shopName
     * @param category
     * @param minPrice
     * @param maxPrice
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean pageSelect(String shopName, String category, Integer minPrice, Integer maxPrice, String sort,Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);

        //2.执行查询
        List<Shop> shopList = shopMapper.list(shopName,category,minPrice,maxPrice,sort);
        Page<Shop> p = (Page<Shop>) shopList;

        //3.封装
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @Override
    public Shop selectById(Integer id) {
        return shopMapper.selectById(id);
    }

    /**
     * 新增商品信息
     * @param shop
     */
    @Override
    public void insert(Shop shop) {
        shop.setCreateTime(LocalDateTime.now());
        shop.setUpdateTime(LocalDateTime.now());
        shopMapper.insert(shop);
    }

    /**
     * 根据id删除商品信息
     * @param ids
     */
    @Override
    public void delete(List<Integer> ids) {
        shopMapper.delete(ids);
    }

    /**
     * 更新商品信息
     * @param shop
     */
    @Override
    public void update(Shop shop) {
        shop.setUpdateTime(LocalDateTime.now());
        shopMapper.update(shop);
    }
}
