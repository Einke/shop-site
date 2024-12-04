package com.xts.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xts.mapper.OrdersMapper;
import com.xts.mapper.PurchaseLogMapper;
import com.xts.pojo.Orders;
import com.xts.pojo.PageBean;
import com.xts.pojo.PurchaseLog;
import com.xts.service.OrdersService;
import com.xts.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private PurchaseLogMapper purchaseLogMapper;

    /**
     * 条件查询全部订单，分页展示
     * 用户名、商品名、创建时间
     * @param shopName
     * @param begin
     * @param end
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean pageSelect(String shopName, Integer state,Integer userId,LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);

        //2.执行查询
        List<Orders> ordersList = ordersMapper.list(shopName, userId,state,begin, end);
        Page<Orders> p = (Page<Orders>) ordersList;

        //3.封装
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Override
    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    /**
     * 新增订单
     * 记录购买日志
     * @param orders
     */
    @Override
    @Transactional  //事务管理
    public void insert(Orders orders) {
        //orders.setUserId(userId);
        orders.setCreateTime(LocalDateTime.now());
        orders.setState(3);
        ordersMapper.insert(orders);

        //购买日志
        PurchaseLog purchaseLog = new PurchaseLog();
        purchaseLog.setUserId(orders.getUserId());
        purchaseLog.setShopId(orders.getShopId());
        purchaseLog.setQuantity(orders.getQuantity());
        purchaseLog.setTotalPrice(orders.getTotalPrice());
        purchaseLog.setOperateTime(LocalDateTime.now());
        purchaseLogMapper.insert(purchaseLog);
    }

    /**
     * 删除订单
     * @param ids
     */
    @Override
    public void delete(List<Integer> ids) {
        ordersMapper.delete(ids);
    }

    /**
     * 更新订单信息
     * @param orders
     */
    @Override
    public void update(Orders orders) {
        ordersMapper.update(orders);
    }

    /**
     * 更新订单状态为2
     * @param id
     */
    @Override
    public void updateState2(Integer id) {
        log.info("已更新订单{}状态为已发货",id);
        ordersMapper.updateState2(id);
    }

    /**
     * 更改订单状态为1
     * @param id
     */
    @Override
    public void updateState1(Integer id) {
        ordersMapper.updateState1(id);
    }

    /**
     * 管理员的查询
     * @param username
     * @param state
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean adminPageSelect(String username, Integer state, Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);

        //2.执行查询
        List<Orders> ordersList = ordersMapper.adminList(username, state);
        Page<Orders> p = (Page<Orders>) ordersList;

        //3.封装
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }
}
