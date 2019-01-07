package com.ystmall.service;

import com.github.pagehelper.PageInfo;
import com.ystmall.common.ServerResponse;
import com.ystmall.pojo.Product;
import com.ystmall.vo.ProductDetailVo;

public interface IProductService{

    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProdectByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
