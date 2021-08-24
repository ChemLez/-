package cn.lizhi.serviceEdu.service;

import cn.lizhi.serviceEdu.entity.EduCourse;
import cn.lizhi.serviceEdu.vo.course.CourseInfo;
import cn.lizhi.serviceEdu.vo.course.CoursePublishVo;
import cn.lizhi.serviceEdu.vo.course.CourseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
public interface EduCourseService extends IService<EduCourse> {

    String addCourseInfo(CourseInfo courseInfo);

    CourseInfo getCourseInfo(String id);

    void updateCourseInfo(CourseInfo courseInfo);

    CoursePublishVo getCoursePublishVoById(String courseId);

    void publishCourseById(String courseId);

    void removeCourse(String courseId);

    void pageQuery(Page<EduCourse> pageParam, CourseQuery courseQuery);

    List<EduCourse> selectHotCourse();
}
