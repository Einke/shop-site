package com.xts.controller;

import com.xts.pojo.Form;
import com.xts.pojo.PageBean;
import com.xts.pojo.Result;
import com.xts.service.FormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin(origins="*")
@RestController
public class FormController {

    @Autowired
    private FormService formService;

    /**
     * 查询排名数据
     * @return
     */
    @GetMapping("/admin/form/rank")
    public Result selectRanking(){
        log.info("查询销售统计报表的排名数据");
        List<Form> forms = formService.selectRanking();
        return Result.success(forms);
    }

    /**
     * 查询分类的数据
     * @return
     */
    @GetMapping("/admin/form/category")
    public Result selectCategory(){
        log.info("查询销售统计报表的饼状图数据");
        List<Form> forms = formService.selectCategory();
        return Result.success(forms);
    }

    /**
     * 分页查询每个商品的销售详情数据
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/admin/form/detail")
    public Result selectDetail(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer pageSize){
        log.info("分页查询销售统计报表的销售详情数据");
        PageBean pageBean = formService.selectDetail(page, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 查询基本信息
     * @return
     */
    @GetMapping("/admin/form/basic")
    public Result selectBasic(){
        log.info("查询销售统计报表的基本信息");
        Form form = formService.selectBasic();
        return Result.success(form);
    }
}
