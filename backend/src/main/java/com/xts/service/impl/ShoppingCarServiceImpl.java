package com.xts.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xts.mapper.ShoppingCarMapper;
import com.xts.pojo.PageBean;
import com.xts.pojo.ShoppingCar;
import com.xts.service.ShoppingCarService;
import com.xts.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCarServiceImpl implements ShoppingCarService {

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    /**
     * 查询购物车信息 分页
     * 纯分页不设置条件查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean pageSelect(String shopName,Integer userId, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);

        //2.执行查询
        List<ShoppingCar> shoppingCarList = shoppingCarMapper.list(shopName,userId,begin,end);
        Page<ShoppingCar> p = (Page<ShoppingCar>) shoppingCarList;

        //3.封装
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

    /**
     * 根据id查询购物车信息
     * @param id
     * @return
     */
    @Override
    public ShoppingCar selectById(Integer id) {
        return shoppingCarMapper.selectById(id);
    }

    /**
     * 新增购物车信息
     * @param shoppingCar
     */
    @Override
    public void insert(ShoppingCar shoppingCar) {
        shoppingCar.setCreateTime(LocalDateTime.now());
        shoppingCar.setUpdateTime(LocalDateTime.now());
        shoppingCarMapper.insert(shoppingCar);
    }

    /**
     * 根据id删除购物车信息
     * @param ids
     */
    @Override
    public void delete(List<Integer> ids) {
        shoppingCarMapper.delete(ids);
    }

    /**
     * 更新购物车信息
     * @param shoppingCar
     */
    @Override
    public void update(ShoppingCar shoppingCar) {
        shoppingCar.setUpdateTime(LocalDateTime.now());
        shoppingCarMapper.update(shoppingCar);
    }
}
