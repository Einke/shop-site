package com.xts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private Integer id;
    private Integer userId;
    private String username;
    private Integer shopId;
    private String shopName;
    private Integer quantity;
    private Integer price;
    private Integer totalPrice;
    private Integer state;//1代表已送达，2代表已发货，3代表未发货
    private LocalDateTime createTime;
}
