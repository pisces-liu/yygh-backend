package com.atguigu.yygh.oss.controller;

import com.atguigu.yygh.common.config.result.R;
import com.atguigu.yygh.oss.service.FileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/oss/file")
public class FileUploadController {

    @Resource
    private FileService fileService;

    @RequestMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        String uploadUrl = fileService.upload(file);
        return R.ok().message("文件上传成功").data("url", uploadUrl);
    }
}
