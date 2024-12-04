package com.xts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrowseLog {
    private Integer id;
    private Integer userId;
    private String username;
    private Integer shopId;
    private String ShopName;
    private LocalDateTime operateTime;
    private long costTime;
}
