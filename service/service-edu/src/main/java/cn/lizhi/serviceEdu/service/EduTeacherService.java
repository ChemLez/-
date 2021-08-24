package cn.lizhi.serviceEdu.service;

import cn.lizhi.serviceEdu.entity.EduTeacher;
import cn.lizhi.serviceEdu.vo.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author liz
 * @since 2020-12-18
 */
public interface EduTeacherService extends IService<EduTeacher> {

    /**
     * 讲师模糊查询
     * @param pageParam 分页信息设定
     * @param teacherQuery 条件对象
     */
    void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery);

    List<EduTeacher> selectHotTeacher();
}
