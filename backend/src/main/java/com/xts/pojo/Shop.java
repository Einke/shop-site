package com.xts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    private Integer id;  // 主键ID
    private String shopName;  // 名称
    private String category;  // 商品分类
    private Integer price;  // 商品价格
    private Integer profit; //商品利润
    private String image;  // 商品图片URL
    private String description;     //描述
    private LocalDateTime createTime;   //创建时间
    private LocalDateTime updateTime;   //修改时间
}
