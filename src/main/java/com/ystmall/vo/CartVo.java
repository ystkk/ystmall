package com.ystmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用来存放购物车集合
 */
public class CartVo {
    //购物车集合
    private List<CartProductVo> cartProductVoList;
    //总价
    private BigDecimal cartTotalPrice;
    //是否都勾选
    private Boolean allChecked;
    //图片地址
    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
