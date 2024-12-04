package com.xts.controller;

import com.xts.pojo.PageBean;
import com.xts.pojo.Result;
import com.xts.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 管理员分页查询浏览日志
     * @param username
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/admin/browse")
    public Result browsePageSelect(String username,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("管理员分页查询浏览日志，参数：{}，{}，{}",  username, page, pageSize);
        PageBean pageBean = logService.browsePageSelect( username,page, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 管理员分页查询购买日志
     * @param username
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/admin/purchase")
    public Result purchasePageSelect(String username,
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer pageSize){
        log.info("管理员分页查询购买日志，参数：{}，{}，{}",  username, page, pageSize);
        PageBean pageBean = logService.purchasePageSelect( username,page, pageSize);
        return Result.success(pageBean);
    }


}
