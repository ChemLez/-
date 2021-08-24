package cn.lizhi.serviceEdu.service.impl;

import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceEdu.entity.EduCourse;
import cn.lizhi.serviceEdu.entity.EduCourseDescription;
import cn.lizhi.serviceEdu.mapper.EduCourseMapper;
import cn.lizhi.serviceEdu.service.EduChapterService;
import cn.lizhi.serviceEdu.service.EduCourseDescriptionService;
import cn.lizhi.serviceEdu.service.EduCourseService;
import cn.lizhi.serviceEdu.service.EduVideoService;
import cn.lizhi.serviceEdu.vo.course.CourseInfo;
import cn.lizhi.serviceEdu.vo.course.CoursePublishVo;
import cn.lizhi.serviceEdu.vo.course.CourseQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.BiIntFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService; // 注入小节

    @Autowired
    private EduChapterService eduChapterService; // 注入章节


    @Override
    public String addCourseInfo(CourseInfo courseInfo) {

        // 课程信息添加
        EduCourse eduCourse = new EduCourse(); // 课程实体类
        BeanUtils.copyProperties(courseInfo, eduCourse); // 作用是映射到course表中
        int count = baseMapper.insert(eduCourse);

        if (count == 0) {
            throw new GuliException(20001, "课程添加失败");
        }
        String id = eduCourse.getId(); // 目的将course的id和description的id做对应 一对一的映射关系 course and description 是一对一关系

        // 课程描述添加
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfo.getDescription());
        eduCourseDescription.setId(id);
        boolean save = eduCourseDescriptionService.save(eduCourseDescription);

        if (!save) {
            throw new GuliException(20001, "课程描述添加失败");
        }
        return id;
    }

    /**
     * 根据课程id 对courseVo对象进行封装
     * @param id
     * @return
     */
    @Override
    public CourseInfo getCourseInfo(String id) {
        CourseInfo courseInfo = new CourseInfo();
        EduCourse eduCourse = baseMapper.selectById(id);
        if (eduCourse == null) {
            throw new GuliException(20001, "不存在该课程信息");
        }
        String description = eduCourseDescriptionService.getById(id).getDescription();
        courseInfo.setDescription(description);
        BeanUtils.copyProperties(eduCourse, courseInfo);
        return courseInfo;
    }

    @Override
    public void updateCourseInfo(CourseInfo courseInfo) {

        // 对数据库对应的信息进行修改
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfo, eduCourse);
        int count = baseMapper.updateById(eduCourse);
        if (count == 0) {
            throw new GuliException(20001, "更新失败");
        }

        // 修改课程描述信息
        EduCourseDescription description = new EduCourseDescription();
        description.setId(eduCourse.getId());
        description.setId(courseInfo.getDescription());
        eduCourseDescriptionService.updateById(description); // 既然课程能更新成功那么，课程附带的描述也能更新成功
    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String courseId) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(courseId);
        return publishCourseInfo;
    }

    @Override
    public void publishCourseById(String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        Integer count = baseMapper.updateById(eduCourse);
        if (count == null || count == 0) {
            throw new GuliException(20001, "发布失败");
        }
    }


    @Override
    public void removeCourse(String courseId) {
        // 1.根据课程id删除小节 - 批量删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        // 2.根据课程id删除章节 - 批量删除章节
        eduChapterService.removeChapterByCourseId(courseId);

        // 3.删除课程描述
        eduCourseDescriptionService.removeById(courseId);

        // TODO:删除服务器的课程封面

        // 4.最后删除课程
        int result = baseMapper.deleteById(courseId);
        if (result == 0) {
            throw new GuliException(20001, "课程删除失败");
        }
    }

    @Override
    public void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>(); // 条件变量
        if (courseQuery == null) { // 查询所有
            baseMapper.selectPage(pageParam, queryWrapper); // 结果封装到pageParam中
            return;
        }

        String title = courseQuery.getTitle();
        String subjectId = courseQuery.getSubjectId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String teacherId = courseQuery.getTeacherId();

        if (!StringUtils.isEmpty(title)) { // 题目不空
            queryWrapper.like("title", title);
        }

        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id", subjectId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }

        if (!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id", teacherId);
        }

        baseMapper.selectPage(pageParam, queryWrapper); // 结果封装到pageParam中
    }

    @Override
    public List<EduCourse> selectHotCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 8");
        List<EduCourse> eduCourses = baseMapper.selectList(wrapper);
        return eduCourses;
    }

}
