package cn.lizhi.serviceEdu.vo.excel;


import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "SubjectData对象", description = "课程分类一、二级标签")
public class SubjectData  {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "一级标签", index = 0) // 该属性代表表中的索引为0 的列
    private String parentTag;

    @ExcelProperty(value = "二级标签", index = 1) // 该属性代表表中的索引为1 的列
    private String childrenTag;

}
