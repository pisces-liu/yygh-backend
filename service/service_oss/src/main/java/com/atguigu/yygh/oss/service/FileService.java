package com.atguigu.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传文件至阿里云
     *
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
