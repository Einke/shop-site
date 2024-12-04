package com.xts.controller;

import com.xts.pojo.PageBean;
import com.xts.pojo.Result;
import com.xts.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 管理员的搜索
     * @param username
     * @param state
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/admin/orders")
    public Result adminPageSelect(String username,Integer state,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("管理员分页查询订单，参数：{}，{}，{},{}",  username,state, page, pageSize);
        PageBean pageBean = ordersService.adminPageSelect( username,state,page, pageSize);
        return Result.success(pageBean);
    }

}
