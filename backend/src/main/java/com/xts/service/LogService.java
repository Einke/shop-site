package com.xts.service;

import com.xts.pojo.PageBean;

public interface LogService {

    /**
     * 分页查询浏览日志
     * @param username
     * @param page
     * @param pageSize
     * @return
     */
    PageBean browsePageSelect(String username, Integer page, Integer pageSize);

    /**
     * 分页查询购买日志
     * @param username
     * @param page
     * @param pageSize
     * @return
     */
    PageBean purchasePageSelect(String username, Integer page, Integer pageSize);


}
