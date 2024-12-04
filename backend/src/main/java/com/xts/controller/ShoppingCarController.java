package com.xts.controller;

import com.xts.pojo.PageBean;
import com.xts.pojo.Result;
import com.xts.pojo.ShoppingCar;
import com.xts.service.ShoppingCarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class ShoppingCarController {

    @Autowired
    private ShoppingCarService shoppingCarService;

    /**
     * 查询全部购物车 分页展示
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/car")
    public Result pageSelect(String shopName,Integer userId,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询购物车，参数：{}，{},{},{},{},{}", shopName,userId,begin,end,page, pageSize);
        PageBean pageBean = shoppingCarService.pageSelect(shopName,userId,begin,end, page, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 根据id查询购物车数据
     * @param id
     * @return
     */
    @GetMapping("/car/{id}")
    public Result selectById(@PathVariable Integer id){
        log.info("根据id查询购物车：{}",id);
        ShoppingCar shoppingCar = shoppingCarService.selectById(id);
        return Result.success(shoppingCar);
    }

    /**
     * 添加购物车数据
     * @param shoppingCar
     * @return
     */
    @PostMapping("/car")
    public Result insert(@RequestBody ShoppingCar shoppingCar){
        log.info("添加购物车信息：{}",shoppingCar);
        shoppingCarService.insert(shoppingCar);
        return Result.success();
    }

    /**
     * 根据id删除购物车，可批量
     * @param ids
     * @return
     */
    @DeleteMapping("/car/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("删除购物车信息：{}",ids);
        shoppingCarService.delete(ids);
        return Result.success();
    }

    /**
     * 修改购物车数据
     * @param shoppingCar
     * @return
     */
    @PutMapping("/car")
    public Result update(@RequestBody ShoppingCar shoppingCar){
        log.info("修改购物车信息：{}",shoppingCar);
        shoppingCarService.update(shoppingCar);
        return Result.success();
    }

}
