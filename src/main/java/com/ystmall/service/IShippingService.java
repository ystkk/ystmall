package com.ystmall.service;

import com.github.pagehelper.PageInfo;
import com.ystmall.common.ServerResponse;
import com.ystmall.pojo.Shipping;


public interface IShippingService {

    ServerResponse add(Integer userId, Shipping shipping);
    ServerResponse<String> del(Integer userId,Integer shippingId);
    ServerResponse update(Integer userId, Shipping shipping);
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
