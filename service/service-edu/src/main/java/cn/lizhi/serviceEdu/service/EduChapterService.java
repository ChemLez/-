package cn.lizhi.serviceEdu.service;

import cn.lizhi.serviceEdu.entity.EduChapter;
import cn.lizhi.serviceEdu.vo.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
public interface EduChapterService extends IService<EduChapter> {
    List<ChapterVo> getChapter(String courseId);

    boolean removeChapterById(String id);

    void removeChapterByCourseId(String courseId);
}
