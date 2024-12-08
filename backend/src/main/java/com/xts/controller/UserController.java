package com.xts.controller;

import com.xts.pojo.PageBean;
import com.xts.pojo.Result;
import com.xts.pojo.User;
import com.xts.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询全部用户数据
     * 条件查询分页展示
     * @return
     */
    @GetMapping("/user")
    public Result pageSelect(String username,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询，参数：{}，{}，{}", username,  page, pageSize);
        PageBean pageBean = userService.pageSelect(username, page, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 根据id查询用户数据
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    public Result selectById(@PathVariable Integer id){
        log.info("根据id查询用户：{}",id);
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 添加用户数据
     * @param user
     * @return
     */
    @PostMapping("/user")
    public Result insert(@RequestBody User user){
        log.info("添加用户信息：{}",user);
        userService.insert(user);
        return Result.success();
    }

    /**
     * 根据id删除用户
     * @param ids
     * @return
     */
    @DeleteMapping("/user/{ids}")
    public Result delete(@PathVariable List<Integer>ids){
        log.info("删除用户：{}",ids);
        userService.delete(ids);
        return Result.success();
    }

    /**
     * 修改用户数据
     * @param user
     * @return
     */
    @PutMapping("/user")
    public Result update(@RequestBody User user){
        log.info("修改用户数据：{}",user);
        userService.update(user);
        return Result.success();
    }
}
