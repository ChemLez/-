package cn.lizhi.serviceEdu.vo.course;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "课程基本信息",description = "用于编辑课程基本信息的表单对象")
public class CourseInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "课程讲师ID")
    private String teacherId;

    @ApiModelProperty(value = "课程专业ID") // 二级课程分类的id
    private String subjectId;

    @ApiModelProperty(value = "课程专业父级ID") // 一级课程分类的id
    private String subjectParentId;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price; // 价钱，精度需要达到0.01，故需要设定 变量类型为 BigDecimal

    @ApiModelProperty(value = "课程简介") // 主要就是这个字段；用于前端数据的封装
    private String description;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面路径")
    private String cover;

}
