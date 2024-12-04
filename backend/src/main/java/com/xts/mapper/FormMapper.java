package com.xts.mapper;

import com.xts.pojo.Form;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FormMapper {

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
     * @return
     */
    List<Form> selectDetail();

    /**
     * 查询基本信息
     * @return
     */
    Form selectBasic();
}
