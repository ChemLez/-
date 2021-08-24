package cn.lizhi.serviceEdu.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Teacher查询对象", description = "讲师查询对象封装")
public class TeacherQuery {

    /**
     * 设定的条件对象中，包含 name、level、begin、end
     */

    @ApiModelProperty(value = "讲师名称，模糊查询")
    private String name;

    @ApiModelProperty(value = "讲师等级 1高级头衔 2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "查询开始时间", example = "2010-10-10 21:09:01")
    private String begin; //注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String end;
}
