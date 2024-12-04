package com.xts.controller;

import com.xts.anno.BrowseLog;
import com.xts.pojo.PageBean;
import com.xts.pojo.Result;
import com.xts.pojo.Shop;
import com.xts.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 查询商品数据，条件查询分页展示
     * @param shopName
     * @param category
     * @param minPrice
     * @param maxPrice
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/shop")
    public Result pageSelect(String shopName,String category,Integer minPrice,Integer maxPrice,String sort,
                             @RequestParam(defaultValue = "1") Integer page,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询，参数：{}，{}，{}，{}，{},{},{}", shopName, category,minPrice,maxPrice,sort, page, pageSize);
        PageBean pageBean = shopService.pageSelect(shopName, category,minPrice, maxPrice, sort,page, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 根据id查询商品数据
     * @param id
     * @return
     */
    @BrowseLog
    @GetMapping("/shop/{id}")
    public Result selectById(@PathVariable Integer id){
        log.info("根据id查询商品：{}",id);
        Shop shop = shopService.selectById(id);
        return Result.success(shop);
    }

    /**
     * 添加商品数据
     * @param shop
     * @return
     */
    @PostMapping("/shop")
    public Result insert(@RequestBody Shop shop){
        log.info("添加商品信息：{}",shop);
        shopService.insert(shop);
        return Result.success();
    }

    /**
     * 根据id删除商品，可批量
     * @param ids
     * @return
     */
    @DeleteMapping("/shop/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("删除商品：{}",ids);
        shopService.delete(ids);
        return Result.success();
    }

    /**
     * 修改商品数据
     * @param shop
     * @return
     */
    @PutMapping("/shop")
    public Result update(@RequestBody Shop shop){
        log.info("修改商品信息：{}",shop);
        shopService.update(shop);
        return Result.success();
    }
}
