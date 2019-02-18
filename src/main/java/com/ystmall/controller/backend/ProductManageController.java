package com.ystmall.controller.backend;

import com.google.common.collect.Maps;
import com.ystmall.common.ServerResponse;
import com.ystmall.pojo.Product;
import com.ystmall.service.IFileService;
import com.ystmall.service.IProductService;
import com.ystmall.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 产品后台管理
 * @author Shengtong Yuan
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     *  v2.0 portal保存商品
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(Product product){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * v2.0 portal修改产品销售状态
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(Integer productId, Integer status){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iProductService.setSaleStatus(productId, status);
    }

    /**
     * v2.0 portal获取产品详情
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(Integer productId){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iProductService.manageProductDetail(productId);
    }

    /**
     * v2.0 backend查询产品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iProductService.getProductList(pageNum,pageSize);
    }

    /**
     * v2.0 backend 搜索产品
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){

        //全部通过AuthorityInterceptor验证是否登录以及权限
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    /**
     * v2.0 backend 文件上传
     * @param httpServletRequest
     * @param file
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    //HttpServletRequest request动态的创建一个相对路径
    public ServerResponse upload(HttpServletRequest httpServletRequest,@RequestParam(value = "upload_file", required = false) MultipartFile file){

        //全部通过AuthorityInterceptor验证是否登录以及权限

        //从request的session中拿到上下文，上传到upload文件夹
        String path = httpServletRequest.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     * v2.0 富文本上传,使用simditor插件
     * @param httpServletRequest
     * @param file
     * @return
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    //HttpServletRequest request动态的创建一个相对路径
    public Map richtextImgUpload(HttpServletRequest httpServletRequest,  HttpServletResponse httpServletResponse,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file){

        //全部通过AuthorityInterceptor验证是否登录以及权限

        Map resultMap = Maps.newHashMap();

        //富文本中对于返回值有自己的要求，使用simditor所以按照simditor要求返回
        //{
        //  "success":true/false,
        //  "msg":"error message",
        //  "file_path": "[real file path]"
        //}


        //从request的session中拿到上下文，上传到upload文件夹
        String path = httpServletRequest.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file,path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path",url);

        httpServletResponse.addHeader("Access-Control-Allow-Headers", "X-File-Name");

        return resultMap;

    }

}
