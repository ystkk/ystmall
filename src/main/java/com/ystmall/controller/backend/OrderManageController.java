package com.ystmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.ystmall.common.ServerResponse;
import com.ystmall.service.IOrderService;
import com.ystmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 订单后台管理
 * @author Shengtong Yuan
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iOrderService.manageList(pageNum,pageSize);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(Long orderNo){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iOrderService.manageDetail(orderNo);
    }



    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iOrderService.manageSearch(orderNo,pageNum,pageSize);
    }



    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(Long orderNo){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iOrderService.manageSendGoods(orderNo);
    }

}
