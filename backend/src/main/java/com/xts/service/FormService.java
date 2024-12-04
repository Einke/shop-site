package com.xts.service;

import com.xts.pojo.Form;
import com.xts.pojo.PageBean;

import java.util.List;

public interface FormService {

    /**
     * 查询排名数据
     * @return
     */
    List<Form> selectRanking();

    /**
     * 查询分类的数据
     * @return
     */
    List<Form> selectCategory();

    /**
     * 分页查询每个商品的销售详情数据
     * @param page
     * @param pageSize
     * @return
     */
    PageBean selectDetail(Integer page, Integer pageSize);

    /**
     * 查询基本信息
     * @return
     */
    Form selectBasic();
}
