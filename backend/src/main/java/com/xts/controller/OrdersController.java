package com.xts.controller;

import com.xts.pojo.*;
import com.xts.service.EmailService;
import com.xts.service.impl.EmailServiceImpl;
import com.xts.service.OrdersService;
import com.xts.service.ShopService;
import com.xts.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private EmailService emailService;

    /**
     * 条件查询全部订单信息，分页展示
     * @param shopName
     * @param state
     * @param userId
     * @param begin
     * @param end
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/orders")
    public Result pageSelect(String shopName,Integer state,Integer userId,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询订单，参数：{}，{}，{}，{},{},{},{}",  shopName,userId, state,begin, end, page, pageSize);
        PageBean pageBean = ordersService.pageSelect( shopName,state,userId, begin, end, page, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 根据id查询订单数据
     * @param id
     * @return
     */
    @GetMapping("/orders/{id}")
    public Result selectById(@PathVariable Integer id){
        log.info("根据id查询订单：{}",id);
        Orders orders = ordersService.selectById(id);
        return Result.success(orders);
    }

    /**
     * 添加订单数据
     * 发送电子邮件
     * @param orders
     * @return
     */
    @PostMapping("/orders")
    @Transactional //事务管理
    public Result insert(@RequestBody Orders orders){
        log.info("添加订单信息：{}",orders);
        ordersService.insert(orders);

        //发送邮件先获取信息
        User user = userService.selectById(orders.getUserId());
        Shop shop = shopService.selectById(orders.getShopId());
        String email = user.getEmail();
        String content = String.format("""
            <h3>尊敬的%s：</h3>
            <p>您的订单已发货，</p>
            <p>订单金额：%s </p>
            <p>购买商品：%s个%s </p>
            <p>发货时间：%s </p>
            <p>感谢您的购买！</p>
            <p>    --来自xts的购物网站</p>
            """,
                user.getUsername(),
                orders.getTotalPrice(),
                orders.getQuantity(),
                shop.getShopName(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        try{
            boolean send = emailService.sendSimpleEmail(email, "订单邮件", content);
            //更改订单状态
            if(send) {
                ordersService.updateState2(orders.getId());
            }
        }catch (Exception e){
            log.error("创建订单后发送邮件失败");
        }

        return Result.success();
    }

    /**
     * 根据id删除订单，可批量
     * @param ids
     * @return
     */
    @DeleteMapping("/orders/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("删除订单：{}",ids);
        ordersService.delete(ids);
        return Result.success();
    }

    /**
     * 修改订单数据
     * @param orders
     * @return
     */
    @PutMapping("/orders")
    public Result update(@RequestBody Orders orders){
        log.info("修改订单信息：{}",orders);
        ordersService.update(orders);
        return Result.success();
    }

    @PutMapping("/orders/{id}")
    public Result updateState1(@PathVariable Integer id){
        log.info("更改订单{}状态为已送达",id);
        ordersService.updateState1(id);
        return Result.success();
    }
}
