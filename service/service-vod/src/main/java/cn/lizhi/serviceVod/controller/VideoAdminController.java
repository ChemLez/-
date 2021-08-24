package cn.lizhi.serviceVod.controller;

import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceVod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"文件视频上传模块"})
@RestController
@RequestMapping("/serviceVod/vod/video")
@CrossOrigin
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    @PostMapping("upload")
    public Result uploadVideo(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {

        String videoId = videoService.uploadVideo(file); // 视频上传，返回上传视频的videoId

        return Result.ok().message("视频上传成功").data("videoId", videoId);
    }

    @DeleteMapping("{videoId}")
    public Result removeVideo(@ApiParam(name = "videoId", value = "云端视频id", required
            = true) @PathVariable String videoId) {
        videoService.removeVideo(videoId);
        return Result.ok().message("视频删除成功");

    }

    @DeleteMapping("delete-batch")
    @ApiOperation(value = "批量删除小节")
    public Result deleteBatch(@RequestParam("videoIdList") @ApiParam(required = true) List<String> videoList) {
        videoService.removeMoreAlyVideo(videoList);
        return Result.ok();
    }
}
