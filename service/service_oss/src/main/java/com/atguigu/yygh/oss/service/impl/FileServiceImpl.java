package com.atguigu.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.yygh.oss.service.FileService;
import com.atguigu.yygh.oss.util.ConstantProperties;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file) {
        // 获取阿里云 oss 相关信息
        String endPoint = ConstantProperties.END_POINT;
        String accessKeyId = ConstantProperties.ACCESS_KEY_ID;
        String accessKeySecret = ConstantProperties.ACCESS_KEY_SECRET;
        String bucketName = ConstantProperties.BUCKET_NAME;


        try {
            // 通过文件流的方式上传图片到 阿里云OSS
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
            // 将文件转换为输入流
            InputStream inputStream = file.getInputStream();
            // 获取文件原名称
            String filename = file.getOriginalFilename();
            // 使用 UUID 生成唯一ID，避免上传的文件重名
            String uuid = UUID.randomUUID().toString().replace("-", "");
            filename = uuid + filename;
            // 按照当前日前，创建文件夹，上传到指定文件夹里面
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            filename = timeUrl + "/" + filename;
            // 调用 oss 方法实现上传文件
            ossClient.putObject(bucketName, filename, inputStream);
            // 关闭 OSSClient
            ossClient.shutdown();

            // 上传完之后返回完整的文件路径给前端
            // 路径例如：// https://yygh-atguigu.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://" + bucketName + "." + endPoint + "/" + filename;
            return url;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }


    }
}
