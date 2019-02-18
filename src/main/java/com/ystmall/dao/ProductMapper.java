package com.ystmall.dao;

import com.ystmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> selectByNameAndProductId(@Param("productName") String productName, @Param("productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param("productName") String productName,@Param("categoryIdList") List<Integer> categoryIdList);

    /**
     * v2.0 定时关单-通过产品id查询库存
     * note: 要使用Integer因为如果产品下架返回null不会报错
     * @param id
     * @return
     */
    Integer selectStockByProductId(Integer id);
}