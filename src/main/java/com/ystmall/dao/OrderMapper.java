package com.ystmall.dao;

import com.ystmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /**
     * 根据用户名和订单号查询订单
     * @param userId
     * @param orderNo
     * @return
     */
    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    /**
     * 通过订单号查询订单
     * @return
     */
    Order selectByOrderNo(Long orderNo);

    /**
     * 通过用户名查询订单列表
     * @return
     */
    List<Order> selectByUserId(Integer userId);

    /**
     * 查询所有订单
     * @return
     */
    List<Order> selectAllOrder();
}