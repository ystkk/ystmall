package com.ystmall.service.impl;

import com.google.common.collect.Lists;
import com.ystmall.service.IFileService;
import com.ystmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 返回上传之后的文件名
     */
    public String upload(MultipartFile file, String path){

        //拿到上传文件的原始名
        String fileName = file.getOriginalFilename();
        //扩展名.返回最后以.开头的字符串
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString() + "." +fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名：{},上传的路径：{},新文件名：{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try{
            //文件已经上传成功
            file.transferTo(targetFile);

            //将targetFile上传到我们的FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //上传完之后，删除upload下面的文件(tomcat中)
            targetFile.delete();

        } catch (IOException e){
            logger.error("上传文件异常", e);
        }

        return targetFile.getName();
    }
}
