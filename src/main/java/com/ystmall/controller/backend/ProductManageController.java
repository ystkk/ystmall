package com.ystmall.controller.backend;

import com.google.common.collect.Maps;
import com.ystmall.common.Const;
import com.ystmall.common.ResponseCode;
import com.ystmall.common.ServerResponse;
import com.ystmall.pojo.Product;
import com.ystmall.pojo.User;
import com.ystmall.service.IFileService;
import com.ystmall.service.IProductService;
import com.ystmall.service.IUserService;
import com.ystmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 产品后台管理
 * @author Shengtong Yuan
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    /**
     * 保存商品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please Log In");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //add product
            return iProductService.saveOrUpdateProduct(product);
        } else{
            return ServerResponse.createByErrorMessage("Need Administrator Permission");
        }
    }

    /**
     * 修改产品销售状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please Log In");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus(productId, status);
        } else{
            return ServerResponse.createByErrorMessage("Need Administrator Permission");
        }

    }

    /**
     * 获取产品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please Log In");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //返回产品详情
            return iProductService.manageProductDetail(productId);
        } else{
            return ServerResponse.createByErrorMessage("Need Administrator Permission");
        }

    }

    /**
     * 查询产品列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, 
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, 
                                  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please Log In");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.getProductList(pageNum,pageSize);
        } else{
            return ServerResponse.createByErrorMessage("Need Administrator Permission");
        }

    }

    /**
     * 搜索产品
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session, String productName, Integer productId, 
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, 
                                        @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please Log In");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else{
            return ServerResponse.createByErrorMessage("Need Administrator Permission");
        }

    }

    /**
     * 文件上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    //HttpServletRequest request动态的创建一个相对路径
    public ServerResponse upload(HttpSession session,
                                @RequestParam(value = "upload_file", required = false) MultipartFile file, 
                                HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please Log In");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //从request的session中拿到上下文，上传到upload文件夹
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);

            return ServerResponse.createBySuccess(fileMap);
        } else{
            return ServerResponse.createByErrorMessage("Need Administrator Permission");
        }

    }

    /**
     * 富文本上传,使用simditor插件
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    //HttpServletRequest request动态的创建一个相对路径
    public Map richtextImgUpload(HttpSession session, 
                                @RequestParam(value = "upload_file", required = false) MultipartFile file, 
                                HttpServletRequest request, HttpServletResponse response){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        Map resultMap = Maps.newHashMap();
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","Please Log In");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求，使用simditor所以按照simditor要求返回
        //{
        //  "success":true/false,
        //  "msg":"error message",
        //  "file_path": "[real file path]"
        //}
        if(iUserService.checkAdminRole(user).isSuccess()){
            //从request的session中拿到上下文，上传到upload文件夹
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","upload fail");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg", "upload success");
            resultMap.put("file_path",url);

            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");

            return resultMap;

        } else{
            resultMap.put("success",false);
            resultMap.put("msg","Need Administrator Permission");
            return resultMap;
        }

    }

}
