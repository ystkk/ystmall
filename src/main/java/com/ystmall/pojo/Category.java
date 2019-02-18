package com.ystmall.pojo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//使用lombok生成只作用于id上的hashcode和equals
@EqualsAndHashCode(of = "id")
public class Category {

    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

}