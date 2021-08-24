package cn.lizhi.serviceEdu.controller.front;

import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceEdu.entity.EduCourse;
import cn.lizhi.serviceEdu.entity.EduTeacher;
import cn.lizhi.serviceEdu.service.EduCourseService;
import cn.lizhi.serviceEdu.service.EduTeacherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eduservice/index") // 首页中的Banner需要展示的效果
@CrossOrigin
@Api(tags = {"banner的后端管理接口"})
public class IndexFrontController {


    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;

    @GetMapping
    public Result index() {

        // 查询前8条热门课程
        List<EduCourse> eduCourseList = courseService.selectHotCourse();
        // 查询前4条名师
        List<EduTeacher> eduTeacherList = teacherService.selectHotTeacher();
        return Result.ok().data("hotCourseList", eduCourseList).data("hotTeacherList", eduTeacherList);
    }
}
