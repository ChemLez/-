package cn.lizhi.serviceEdu.controller;

import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceEdu.entity.EduCourse;
import cn.lizhi.serviceEdu.service.EduCourseService;
import cn.lizhi.serviceEdu.vo.course.CourseInfo;
import cn.lizhi.serviceEdu.vo.course.CoursePublishVo;
import cn.lizhi.serviceEdu.vo.course.CourseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
@RestController
@RequestMapping("/serviceEdu/edu-course")
@Api(value = "EduCourse", tags = {"课程管理模块"})
@CrossOrigin // 跨域操作
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    /**
     * 课程信息的添加
     * @param courseInfo
     * @return
     */
    @PostMapping("/addCourseInfo")
    @ApiOperation(value = "课程信息添加")
    public Result addCourseInfo(@RequestBody @ApiParam(name = "courseInfo", value = "课程信息表单", required = true) CourseInfo courseInfo) {

        String id = eduCourseService.addCourseInfo(courseInfo);
        if (!StringUtils.isEmpty(id)) {
            return Result.ok().data("courseID", id);
        } else {
            return Result.error().message("保存失败");
        }

    }

    /**
     * 根据courseId 获取courseVo对象 对前端表单进行封装
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Result getCourseInfo(@PathVariable("id") String id) {
        CourseInfo courseInfo = eduCourseService.getCourseInfo(id);
        return Result.ok().data("item", courseInfo);
    }

    /**
     * 更新信息
     * @param courseInfo
     * @return
     */
    @PutMapping
    public Result updateCourseInfo(@RequestBody CourseInfo courseInfo) {
        eduCourseService.updateCourseInfo(courseInfo);
        return Result.ok();
    }

    /**
     * //根据课程id查询课程确认信息 - 到发布页面时，用于将课程信息进行回显
     * @param id
     * @return
     */
    @GetMapping("getPublishCourseInfo/{id}")
    public Result getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.getCoursePublishVoById(id);
        return Result.ok().data("publishCourse", coursePublishVo);
    }

    /**
     * //课程最终发布
     * //修改课程状态 - 更改课程的状态，表明上线
     * @param courseId
     * @return
     */
    @PostMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable("id") String courseId) {
        eduCourseService.publishCourseById(courseId);
        return Result.ok();
    }


    /**
     * 根据课程Id删除课程
     * 需要删除课程下的小节、章节、课程描述
     * @param courseId
     * @return
     */
    @DeleteMapping("{courseId}")
    public Result deleteCourse(@PathVariable String courseId) {
        eduCourseService.removeCourse(courseId);
        return Result.ok();
    }

    /**
     * 课程的分页展示
     * @param page
     * @param limit
     * @param courseQuery
     * @return
     */
    @ApiOperation(value = "分页课程列表")
    @PostMapping("{page}/{limit}")
    public Result pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    CourseQuery courseQuery) {
        Page<EduCourse> pageParam = new Page<>(page, limit);
        eduCourseService.pageQuery(pageParam, courseQuery);
        List<EduCourse> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return Result.ok().data("total", total).data("rows", records);
    }

}