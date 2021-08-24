package cn.lizhi.serviceEdu.service.impl;

import cn.lizhi.serviceEdu.entity.EduSubject;
import cn.lizhi.serviceEdu.vo.excel.SubjectData;
import cn.lizhi.serviceEdu.listener.SubjectExcelListener;
import cn.lizhi.serviceEdu.mapper.EduSubjectMapper;
import cn.lizhi.serviceEdu.service.EduSubjectService;
import cn.lizhi.serviceEdu.vo.subject.SubjectTreeVo;
import cn.lizhi.serviceEdu.vo.subject.SubjectVo;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-01-16
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Autowired
    private SubjectExcelListener subjectExcelListener;

    /**
     * 课程分类的导入
     * @param file
     * @throws IOException
     */
    @Override
    public void importSubjectData(MultipartFile file) throws IOException {

//        SubjectExcelListener subjectExcelListener = new SubjectExcelListener(service).sheet(); 表示读取的excel中特定的sheet
        EasyExcel.read(file.getInputStream(), SubjectData.class, subjectExcelListener).sheet().doRead();
    }

    /**
     * 层级列表的读取 课程的一级列表和二级列表，其中一级列表包含二级列表
     * @return
     */
    @Override
    public List<SubjectTreeVo> getTreeList() {

        // 最终返回结果 集合中每个对象存放的是一级分类，而在一级分类对象中又存储着二级分类对象vo
        List<SubjectTreeVo> resList = new ArrayList<>();

        // 获取一级分类
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", 0); // parent_id为0 即为 一级标签标识
        queryWrapper.orderByAsc("sort", "id");
        List<EduSubject> parentSubjects = baseMapper.selectList(queryWrapper);

        // 获取二级分类数据
        QueryWrapper<EduSubject> subjectQueryWrapper = new QueryWrapper<>();
        subjectQueryWrapper.ne("parent_id", 0); // 非0即为二级标签
        queryWrapper.orderByAsc("sort", "id");
        List<EduSubject> childrenSubjects = baseMapper.selectList(subjectQueryWrapper);

        // 填充一级分类数据
        for (EduSubject parentSubject : parentSubjects) {

            // 获取到当前一级分类的对象
            // 创建一级类别vo对象 -- 每个一级分类对象对应 结果集中的一个条目；一级下面后续添加上子条目
            SubjectTreeVo subjectTreeVo = new SubjectTreeVo(); // 该一级标签对象 对应的vo对象
            BeanUtils.copyProperties(parentSubject, subjectTreeVo); // 对应properties赋值
            resList.add(subjectTreeVo);

            // 填充二级分类
            List<SubjectVo> subjectVoArrayList = new ArrayList<>(); // 用于填充二级分类的collection
            for (EduSubject childrenSubjectClass : childrenSubjects) {
                // 获取到当前的二级分类对象，目的是为了和外循环中的一级分类对象对应，即是否为其子分类
                if (parentSubject.getId().equals(childrenSubjectClass.getParentId())) { // 是其子类别的情况 二级标签的parent_id和一级标签的id相等时，就代表是当前一级标签下的二级标签

                    // 创建二级分类对象
                    SubjectVo subjectVo = new SubjectVo();
                    BeanUtils.copyProperties(childrenSubjectClass, subjectVo);

                    // 将该二级分类对象 添加进 对应一级分类的对象中
                    subjectVoArrayList.add(subjectVo);
                }

            }
            // 将二级分类结果全部添加进一级分类对象中
            subjectTreeVo.setChildren(subjectVoArrayList);
        }

        return resList;
    }
}
