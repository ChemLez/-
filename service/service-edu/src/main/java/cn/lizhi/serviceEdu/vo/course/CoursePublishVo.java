package cn.lizhi.serviceEdu.vo.course;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "课程发布信息")
@Data
public class CoursePublishVo {

    private String id; // 课程的id
    private String title; // 课程名称
    private String cover; // 课程封面
    private Integer lessonNum; // 课时数
    private String subjectLevelOne; // 课程一级分类
    private String subjectLevelTwo; // 课程二级分类
    private String teacherName; // 讲师名称
    private String price; // 课程价格
}
