package com.ystmall.dao;

import com.ystmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /**
     * 通过用户Id和产品Id查询购物车
     */
    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     * 通过用户名查找该用户所有购物车的集合
     * @param userId
     * @return
     */
    List<Cart> selectCartByUserId(Integer userId);

    /**
     * 通过用户名查看购物车是否全选状态
     * @param userId
     * @return
     */
    int selectCartProductCheckedStatusByUserId(Integer userId);

    /**
     * 通过用户名和商品集合删除购物车
     * @param userId
     * @param productIdList
     * @return
     */
    int deleteByUserIdProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    /**
     * 选择或反选购物车
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("checked") Integer checked);

    /**
     * 查询用户购物车产品数量
     * @param userId
     * @return
     */
    int selectCartProductCount(@Param("userId") Integer userId);

    /**
     * 通过用户名查询已选中的购物车
     * @param userId
     * @return
     */
    List<Cart> selectCheckedCartByUserId(Integer userId);
}