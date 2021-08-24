package cn.lizhi.serviceEdu.controller;


import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceEdu.service.EduSubjectService;
import cn.lizhi.serviceEdu.vo.subject.SubjectTreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-01-16
 */
@RestController
@RequestMapping("/serviceEdu/edu-subject")
@CrossOrigin
@Api(tags = {"课程分类模块"})
public class EduSubjectController {

    @Autowired
    private EduSubjectService service;

    /**
     * 将excel表的内容导入到数据库中
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("addSubject")
    @ApiOperation(value = "Excel批量导入课程分类")
    public Result addSubject(MultipartFile file) throws IOException {
        service.importSubjectData(file);
        return Result.ok();
    }

    @GetMapping("getTreeList")
    @ApiOperation(value = "用户获取课程分类的列表")
    public Result getTreeList() {

        List<SubjectTreeVo> subjectTreeVoList = service.getTreeList();
        return Result.ok().data("items", subjectTreeVoList);
    }

}

