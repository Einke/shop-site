package com.xts.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xts.mapper.BrowseLogMapper;
import com.xts.mapper.PurchaseLogMapper;
import com.xts.pojo.BrowseLog;
import com.xts.pojo.Orders;
import com.xts.pojo.PageBean;
import com.xts.pojo.PurchaseLog;
import com.xts.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private BrowseLogMapper browseLogMapper;

    @Autowired
    private PurchaseLogMapper purchaseLogMapper;

    /**
     * 分页查询浏览日志
     * @param username
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean browsePageSelect(String username, Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);

        //2.执行查询
        List<BrowseLog> browseLogList = browseLogMapper.list(username);
        Page<BrowseLog> p = (Page<BrowseLog>) browseLogList;

        //3.封装
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

    /**
     * 分页查询购买日志
     * @param username
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageBean purchasePageSelect(String username, Integer page, Integer pageSize) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);

        //2.执行查询
        List<PurchaseLog> purchaseLogList = purchaseLogMapper.list(username);
        Page<PurchaseLog> p = (Page<PurchaseLog>) purchaseLogList;

        //3.封装
        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
        return pageBean;
    }

}
