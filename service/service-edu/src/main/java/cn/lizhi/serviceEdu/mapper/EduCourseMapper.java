package cn.lizhi.serviceEdu.mapper;

import cn.lizhi.serviceEdu.entity.EduCourse;
import cn.lizhi.serviceEdu.vo.course.CourseInfo;
import cn.lizhi.serviceEdu.vo.course.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo getPublishCourseInfo(String courseId);

    EduCourse getCourseByCourseId(String courseId);

}
