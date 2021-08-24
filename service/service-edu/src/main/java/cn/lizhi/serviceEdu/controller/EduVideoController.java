package cn.lizhi.serviceEdu.controller;


import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceEdu.entity.EduVideo;
import cn.lizhi.serviceEdu.service.EduVideoService;
import cn.lizhi.serviceEdu.vo.video.VideoInfoVo;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
@RestController
@RequestMapping("/serviceEdu/edu-video")
@Api(value = "video", tags = {"小节模块"})
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;

    @PostMapping
    @ApiOperation(value = "新增课时")
    public Result saveVideo(@RequestBody @ApiParam(name = "videoForm", value = "课时对象", required = true) VideoInfoVo videoInfoVo) {
        videoService.saveVideoInfo(videoInfoVo);
        return Result.ok();
    }

    // 删除小节
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除课时信息")
    public Result removeById(@PathVariable @ApiParam(name = "id",value ="小节id",required = true) String id) {
        boolean result = videoService.removeVideoById(id);
        if(result){
            return Result.ok();
        }else{
            return Result.error().message("删除失败");
        }
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询课时信息")
    public Result getVideoInfoById(@PathVariable("id") @ApiParam(name = "id", value = "课时ID",required = true) String id) {
        EduVideo video = videoService.getVideoInfoById(id);
        return Result.ok().data("item", video);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "小节修改")
    public Result updateVideoById(@PathVariable("id") @ApiParam(name = "id", value = "课时Id") String id,
                                  @RequestBody @ApiParam(name = "videoInfo", value = "小节表单数据",required = true) VideoInfoVo videoInfoVo) {

        videoInfoVo.setId(id);
        videoService.updateVideoInfoById(videoInfoVo);
        return Result.ok();
    }


}

