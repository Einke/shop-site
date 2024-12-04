package com.xts.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xts.mapper.FormMapper;
import com.xts.pojo.Form;
import com.xts.pojo.PageBean;
import com.xts.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormServiceImpl implements FormService {

    @Autowired
    private FormMapper formMapper;

    /**
     * 查询排名数据
     * @return
     */
    @Override
    public List<Form> selectRanking() {
        return formMapper.selectRanking();
    }

    /**
     * 查询分类的数据
     * @return
     */
    @Override
    public List<Form> selectCategory() {
        return formMapper.selectCategory();
    }

    /**
     * 分页查询每个商品的销售详情数据
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean selectDetail(Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);

        //2.执行查询
        List<Form> forms = formMapper.selectDetail();
        Page<Form> p = (Page<Form>) forms;

        //3.封装
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

    /**
     * 查询基本信息
     * @return
     */
    @Override
    public Form selectBasic() {
        return formMapper.selectBasic();
    }
}
