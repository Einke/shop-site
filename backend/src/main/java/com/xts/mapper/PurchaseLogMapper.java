package com.xts.mapper;

import com.xts.pojo.PurchaseLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseLogMapper {

    /**
     * 插入购买日志信息
     * @param purchaseLog
     */
    public void insert(PurchaseLog purchaseLog);

    public List<PurchaseLog> list(String username);
}
