package com.xts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCar {
    private Integer id;
    private Integer userId;
    private Integer shopId;
    private String shopName;
    private Integer price;
    private Integer quantity;
    private LocalDateTime createTime;   //创建时间
    private LocalDateTime updateTime;   //修改时间
}
