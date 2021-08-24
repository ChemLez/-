package cn.lizhi.serviceEdu.vo.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectTreeVo {
    /**
     * 一个SubjectTreeVo 对象 代表一级对象；其中的children用于存放其下的二级分类
     */
    private String id; // 一级课程对应的id
    private String title; // 一级课程对应的title
    private List<SubjectVo> children = new ArrayList<>(); // 其下包含的全部二级目录
}
