package com.xts.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Form {
    private Integer shopId;
    private String shopName;
    private String image;
    private Integer totalQuantity;  //商品销量
    private Integer totalSales;     //商品销售额
    private String category;
    private Integer categoryTotalSales;//分类后的每个类的总销售额
    private Integer totalProfit;    //商品利润
    private Integer salesCount;     //商品订单数量

    //基础信息，总的信息
    private Integer totalOrders;    //总订单数
    private Integer ordersTotalSales;//总销售额
    private Integer ordersTotalProfit;//总利润
    private Integer ordersTotalQuantity;//总销量
    private double averageOrderPrice; //平均客单价
    private double profitRate;      //利润率
    private Integer totalProducts; //总商品数
    private Integer activeProducts;//活跃商品数
}
