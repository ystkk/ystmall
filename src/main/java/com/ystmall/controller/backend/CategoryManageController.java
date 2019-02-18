package com.ystmall.controller.backend;

import com.ystmall.common.ServerResponse;
import com.ystmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 类别后台管理
 * @author Shengtong Yuan
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * v2.0 增加category
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("add_category.do")
    @ResponseBody
    //@RequestParam(value = "parentId", defaultValue = "0") 如果前端没有传parentId，赋予一个默认值0，0是分类根节点
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iCategoryService.addCatagory(categoryName, parentId);
    }

    /**
     * v2.0 设置category的名字
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(Integer categoryId, String categoryName){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iCategoryService.updateCategoryName(categoryId,categoryName);
    }

    /**
     * v2.0 根据传的categoryId获取子节点的categoryId的信息，平级不递归
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        //查询子节点的category信息，不递归，保持平级
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * v2.0 递归深度查询category的id
     * 查询当前节点的id和递归子节点的id
     * 0->10000->1000000
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}
