package cn.lizhi.serviceEdu.service.impl;

import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceEdu.entity.EduChapter;
import cn.lizhi.serviceEdu.entity.EduVideo;
import cn.lizhi.serviceEdu.mapper.EduChapterMapper;
import cn.lizhi.serviceEdu.service.EduChapterService;
import cn.lizhi.serviceEdu.service.EduVideoService;
import cn.lizhi.serviceEdu.vo.chapter.ChapterVo;
import cn.lizhi.serviceEdu.vo.chapter.VideoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {


    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapter(String courseId) {

        List<ChapterVo> res = new ArrayList<>(); // 结果集 - 章节的集合

        // 1.通过courserId获取该课程下的全部章节
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.orderByAsc("id");
        List<EduChapter> eduChapters = baseMapper.selectList(queryWrapper); // 获取到全部的章节对象,每个章节中 - 所有的章节对象
        for (EduChapter eduChapter : eduChapters) { // 将该课程中的全部章节 添加进 res 中，用于后续的遍历  该课程的全部章节
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            res.add(chapterVo);
        }

        // 章节的id对应到小节的chapter_id字段
        // 2.根据video表中的chapter_id，将其进行一一对应，注意一个细节，查找返回时，按照chapter_id，开始遍历res,通过video表中的course_id查询出全部的小节
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("course_id", courseId); // 查询出该课程下的全部小节
        eduVideoQueryWrapper.orderByAsc("id");
        BaseMapper<EduVideo> baseMapper = eduVideoService.getBaseMapper();
        List<EduVideo> eduVideos = eduVideoService.list(eduVideoQueryWrapper);  // 只需要调用一次数据库，查询出该课程对应的全部小节，将小节在业务中进行处理
//        List<EduVideo> eduVideos = baseMapper.selectList(eduVideoQueryWrapper);  // 和上面的使用方式相同；上面调用list，其实就是对mapper的调用

        // 3.将小节和章节进行对应 - 此时每个小节中的 chapter_id和章节的id进行对应
        for (ChapterVo chapterVo : res) { // 取出每个章节
            String id = chapterVo.getId(); // 章节id
            List<VideoVo> children = new ArrayList<>(); // 该章节对应的小节集合
            for (EduVideo eduVideo : eduVideos) { // 搜索该章节下的全部小节，添加进去，即进行
                String chapterId = eduVideo.getChapterId();
                if (chapterId.equals(id)) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    children.add(videoVo);
                }
            }
            chapterVo.setChildren(children);
        }
        return res;
    }

    @Override
    public boolean removeChapterById(String id) {

        // 一个章节下存在多个小节 - 每个小节可能存在一个视频
        if (eduVideoService.getCountByChapterId(id)) {
            throw new GuliException(20001, "该分章节下存在视频课程，请先删除视频课程");
        }
        Integer result = baseMapper.deleteById(id);
        return result != null && result > 0;
    }

    /**
     * 批量删除所有的章节
     * @param courseId:课程id
     */
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        baseMapper.delete(queryWrapper);
    }

}
