package cn.lizhi.serviceEdu.listener;

import cn.lizhi.serviceEdu.entity.EduSubject;
import cn.lizhi.serviceEdu.vo.excel.SubjectData;
import cn.lizhi.serviceEdu.service.EduSubjectService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.NoSuchFileException;
import java.util.Map;

@Component
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    /**
     * 进行数据读取的监听 当监听到数据所采取的动作
     */

    @Autowired
    private EduSubjectService service;

    // 创建有参构造，将service在构造方法内传入 当前类没有交给Spring容器管理，所以不能注入service

    @Override
    public void invoke(SubjectData data, AnalysisContext analysisContext) {

        /**
         * 行读取
         */

        if (data == null) { // 如果数据为null，抛出异常
            try {
                throw new NoSuchFileException("data.excel", "课程分类表中无数据", "重新上传");
            } catch (NoSuchFileException e) {
                e.printStackTrace();
            }
        }

        // 添加一级分类，首先判断一级分类是否存在
        assert data != null;
        EduSubject subject = this.isExistSubject(service, data.getParentTag(), "0");
        if (subject == null) { // 表明数据库中不存在该一级分类 则添加此一级分类
            subject = new EduSubject();
            subject.setTitle(data.getParentTag()); // 一级标签
            subject.setParentId("0"); // 一级标签的标志位设置一级标签的标志位 0
            service.save(subject);
        }

        // 添加二级分类
        String parentId = subject.getId(); // 获取到其一级标签的id。将一级标签的id作为二级标签中的parentId
        EduSubject childrenSubject = this.isExistSubject(service, data.getChildrenTag(), parentId); // 判断二级标签是否存在
        if (childrenSubject == null) {
            childrenSubject = new EduSubject();
            childrenSubject.setTitle(data.getChildrenTag());
            childrenSubject.setParentId(parentId);
            service.save(childrenSubject);
        }


    }

    /**
     * 先来到表头信息，数据行。 headMap中封装的是表头信息
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println("head information:" + headMap);
    }

    /**
     * 判断一、二级分类是否存在
     *
     * @param service
     * @param name    分类名称
     * @param id      当前分类的id
     * @return
     */
    private EduSubject isExistSubject(EduSubjectService service, String name, String id) {

        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>(); // 构造条件查询 条件包装器
        wrapper.eq("title", name);
        wrapper.eq("parent_id", id);
        EduSubject subject = service.getOne(wrapper);
        return subject;

    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
