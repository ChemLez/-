package cn.lizhi.serviceEdu.controller;


import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceEdu.entity.EduChapter;
import cn.lizhi.serviceEdu.service.EduChapterService;
import cn.lizhi.serviceEdu.vo.chapter.ChapterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 * 该类用于查找小节等信息
 *
 * @author liz
 * @since 2021-03-01
 */
@RestController
@RequestMapping("/serviceEdu/edu-chapter")
@CrossOrigin
@Api(value = "EduChapter",tags = {"章节管理"})
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;


    /**
     * 获取某一个课程下的所有章节
     *
     * @param courseId
     * @return
     */
    @GetMapping("getChapter/{courseId}")
    @ApiOperation(value = "嵌套章节数据列表") // 方法定义 swagger
    public Result getChapter(@ApiParam(name = "courseId", value = "课程ID", required = true) @PathVariable("courseId") String courseId) {

        List<ChapterVo> chapters = eduChapterService.getChapter(courseId);

        return Result.ok().data("allChapters", chapters);
    }

    /**
     *
     * @param chapter: 章节对象
     * @return
     */
    @PostMapping()
    @ApiOperation(value = "新增章节")
    public Result addChapter(@ApiParam(name = "chapter",value = "章节对象",required = true) @RequestBody EduChapter chapter) {
        eduChapterService.save(chapter);
        return Result.ok();
    }

    /**
     * 获取某一个章节
     * @param id:章节id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation(value = "获取章节")
    public Result getChapterById(@ApiParam(name = "id", value = "章节id", required = true)
                                 @PathVariable("id")
                                         String id) {
        EduChapter chapter = eduChapterService.getById(id);
        return Result.ok().data("item", chapter);
    }


    /**
     * 删除章节 - 于此同时，需要删除章节下的所有小节
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除章节")
    public Result removeChapterById(@ApiParam(name = "id", value = "章节id", required = true)
                                    @PathVariable("id") String id) {

        boolean res = eduChapterService.removeChapterById(id);
        if (res) {
            return Result.ok();
        }
        return Result.error().message("删除失败");
    }

    @PutMapping("{id}")
    @ApiOperation(value = "根据ID修改章节")
    public Result updateChapter(@ApiParam(name = "id", value = "章节ID", required = true)
                                @PathVariable String id,
                                @ApiParam(name = "chapter", value = "章节对象", required = true)
                                @RequestBody EduChapter chapter) {

        chapter.setId(id);
        eduChapterService.updateById(chapter);
        return Result.ok().data("item", null);
    }

}

