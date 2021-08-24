package cn.lizhi.serviceOss.controller;

import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceOss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Api(tags = {"文件上传模块"})
@CrossOrigin
@RequestMapping("/serviceOss/fileOss")
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping("uploadFile")
    @ApiOperation(value = "头像上传")
    public Result uploadOssFile(@ApiParam(name = "file",value = "上传文件",required = true) MultipartFile file) throws IOException {

        String url = ossService.uploadFileAvatar(file);
        return Result.ok().data("url", url);
    }
}
