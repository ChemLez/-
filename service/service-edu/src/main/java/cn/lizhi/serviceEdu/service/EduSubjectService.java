package cn.lizhi.serviceEdu.service;

import cn.lizhi.serviceEdu.entity.EduSubject;
import cn.lizhi.serviceEdu.vo.subject.SubjectTreeVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author liz
 * @since 2021-01-16
 */
public interface EduSubjectService extends IService<EduSubject> {



    void importSubjectData(MultipartFile file) throws IOException;

    List<SubjectTreeVo> getTreeList();

}
