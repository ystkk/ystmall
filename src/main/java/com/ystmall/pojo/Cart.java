package com.ystmall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//使用lombok注解Data，自动生成构造器和getset方法
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;


}