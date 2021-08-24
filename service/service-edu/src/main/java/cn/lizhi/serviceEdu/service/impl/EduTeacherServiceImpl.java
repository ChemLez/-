package cn.lizhi.serviceEdu.service.impl;

import cn.lizhi.serviceEdu.entity.EduTeacher;
import cn.lizhi.serviceEdu.mapper.EduTeacherMapper;
import cn.lizhi.serviceEdu.service.EduTeacherService;
import cn.lizhi.serviceEdu.vo.TeacherQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author liz
 * @since 2020-12-18
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {




    /**
     * 以该类为例；继承 ServiceImpl<EduTeacherMapper, EduTeacher> 该类，扩展到了Mapper接口中的功能;其中EduTeacherMapper继承了BaseMapper<EduTeacher>
     * ServiceImpl实现了EduTeacherService中的方法
     * ServiceImpl -> IService;然后实现的方法中 对mapper进行了调用。同时实现 EduTeacherService中，只需实现我们自定义的方法或者覆盖原有的方法进行定制
     *
     * @param pageParam    分页信息设定
     * @param teacherQuery 条件对象
     */
    @Override
    public void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery) {

        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>(); // 自定义查询条件对象
        queryWrapper.orderByDesc("gmt_create"); // 按照选中字段进行降序排序(按照创建时间降序排序)，参数填写数据库中的字段

        // 条件为空的情况下 - 查询所有
        if (teacherQuery == null) {
            baseMapper.selectPage(pageParam, queryWrapper); //  page信息对象和条件对象，并将条件查询结果封装到pageParam中
            return;
        }

        // 取出条件对象中的条件值
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name); // 模糊查询
        }
        if (!StringUtils.isEmpty(level)) {
            queryWrapper.eq("level", level); // 相等
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin); // 大于
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end); // 小于
        }
        baseMapper.selectPage(pageParam, queryWrapper); // 传入分页信息和条件查询对象，将结果分装到pageParam中
    }

    @Override
    public List<EduTeacher> selectHotTeacher() {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<EduTeacher> eduTeacherList = baseMapper.selectList(wrapper);
        return eduTeacherList;
    }


}
