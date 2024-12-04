package com.xts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseLog {
    private Integer id;
    private Integer userId;
    private String username;
    private Integer shopId;
    private String shopName;
    private Integer quantity;   //买了多少
    private Integer totalPrice;
    private LocalDateTime operateTime;
}
