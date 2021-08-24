package cn.lizhi.serviceEdu.controller;


import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceEdu.entity.EduTeacher;
import cn.lizhi.serviceEdu.service.EduTeacherService;
import cn.lizhi.serviceEdu.vo.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author liz
 * @since 2020-12-18
 */
@RestController
@RequestMapping("/serviceEdu/edu-teacher")
@Api(tags = {"讲师模块"})
@CrossOrigin // 跨域配置
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @GetMapping("/findAll")
    @ApiOperation(value = "所有讲师列表")
    public Result list() {
//        int a = 10 / 0;
        List<EduTeacher> teachers = teacherService.list();
        if (teachers != null && teachers.size() != 0) {

            return Result.ok().data("items", teachers);
        } else {
            return Result.error();
        }
    }

    /**
     * 根据id删除用户信息(逻辑删除)
     *
     * @param id
     * @return
     */
    @DeleteMapping("/remove/{id}")
    @ApiOperation(value = "根据讲师id删除讲师")
    public Result removeById(@ApiParam(name = "id", value = "讲师id", required = true)
                             @PathVariable("id") String id) {

        teacherService.removeById(id);

        return Result.ok();
    }

    /**
     * 根据id更新讲师信息
     *
     * @param teacher
     * @return
     */
    @ApiOperation(value = "讲师信息修改")
    @PostMapping("/update/{id}")
    public Result updateTeacher(@PathVariable("id") @ApiParam(name = "id", value = "讲师Id") String id,
                                @ApiParam(name = "teacher", value = "讲师对象") @RequestBody(required = false) EduTeacher teacher) {
        teacher.setId(id);
        teacherService.updateById(teacher);
        return Result.ok();

    }

    /**
     * 讲师分页信息
     *
     * @param current
     * @param limit
     * @param teacherQuery
     * @return
     */
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    @ApiOperation(value = "自定义条件分页查询")
    public Result pageList(
            @ApiParam(name = "current", value = "当前页码", required = true) // name是参数值;value是description
            @PathVariable(name = "current")
                    Long current,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable(name = "limit")
                    Long limit,
            @ApiParam(name = "teacherQuery", value = "查询对象")
            @RequestBody(required = false) // 将前端的字符串封装为json对象，方便swagger进行测试
                    TeacherQuery teacherQuery

    ) {
        Page<EduTeacher> pageParam = new Page<>(current, limit); // 定义当前页码，每页展示的条目
        teacherService.pageQuery(pageParam, teacherQuery); // 将对象及分页信息封装到pageParam对象中,即 已将分页信息和数据库返回的数据封装到了pageParam中。后续取值时，从该pageParam中获取
        List<EduTeacher> records = pageParam.getRecords();  // 获取记录数
        long total = pageParam.getTotal(); // 总记录数
        long page = pageParam.getCurrent(); // 页面数

        return Result.ok().data("records", records).data("total", total).data("page", page);
    }

    /**
     * 新增教师
     *
     * @param teacher
     * @return
     */
    @ApiOperation(value = "教师新增")
    @PostMapping("/save")
    public Result saveTeacher(@ApiParam(name = "EduTeacher", value = "讲师对象", required = true)
                              @RequestBody EduTeacher teacher) {

        teacherService.save(teacher);
        return Result.ok();
    }

    /**
     * 讲师查询
     * @param id
     * @return
     */
    @GetMapping("/find/{id}") // 采用 RestFul 风格
    @ApiOperation(value = "根据id查询讲师")
    public Result getTeacherById(@PathVariable("id")
                                 @ApiParam(name = "id", value = "讲师id", required = true) String id) {
        EduTeacher teacher = teacherService.getById(id);
        if (teacher != null) {
            return Result.ok().data("teacher", teacher);
        } else {
            return Result.error();
        }

    }

}

