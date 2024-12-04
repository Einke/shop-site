package com.xts.mapper;

import com.xts.pojo.BrowseLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrowseLogMapper {

    /**
     * 插入浏览日志数据
     * @param browseLog
     */
    public void insert(BrowseLog browseLog);

    /**
     * 查询日志信息
     * @return
     */
    public List<BrowseLog> list(String username);
}
