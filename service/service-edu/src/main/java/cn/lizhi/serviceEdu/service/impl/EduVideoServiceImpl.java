package cn.lizhi.serviceEdu.service.impl;

import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceEdu.client.VodClient;
import cn.lizhi.serviceEdu.entity.EduChapter;
import cn.lizhi.serviceEdu.entity.EduVideo;
import cn.lizhi.serviceEdu.mapper.EduVideoMapper;
import cn.lizhi.serviceEdu.service.EduVideoService;
import cn.lizhi.serviceEdu.vo.video.VideoInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {


    @Autowired
    private VodClient vodClient;

    /**
     * @param chapterId: 课程id
     * @return
     */
    @Override
    public boolean getCountByChapterId(String chapterId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        Integer count = baseMapper.selectCount(wrapper);
        return count != null && count > 0;
    }

    @Override
    public void saveVideoInfo(VideoInfoVo videoInfoVo) {
        EduVideo eduVideo = new EduVideo();
        BeanUtils.copyProperties(videoInfoVo, eduVideo);
        boolean result = this.save(eduVideo);
        if (!result) {
            throw new GuliException(20001, "课时信息保存失败");
        }
    }

    @Override
    public EduVideo getVideoInfoById(String id) {
        EduVideo video = this.getById(id);
        if (video == null) {
            throw new GuliException(20001, "该小节不存在");
        }
        return video;
    }

    @Override
    public void updateVideoInfoById(VideoInfoVo videoInfoVo) {
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoVo, video);
        boolean res = this.updateById(video);
        if (!res) {
            throw new GuliException(20001, "小节修改失败");
        }
    }

    /**
     * 根据小节的id 查询出小节，然后再获取小节中的视频id，最后进行删除视频和小节
     * 需要删除的小节
     * TODO:通过远程调用通过VOD删除视频
     * @param id: 小节id
     * @return
     */
    @Override
    public boolean removeVideoById(String id) {
        EduVideo video = baseMapper.selectById(id); // 查询出所要删除的小节
        String videoId = video.getVideoSourceId(); // 小节下视频的id

        // 删除视频
        if (!StringUtils.isEmpty(videoId)) {
            Result result = vodClient.removeVideo(videoId);
            if(result.getCode() == 20001) {
                throw new GuliException(20001,"删除视频失败，熔断器...");
            }
        }
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }


    /**
     * 根据课程id删除旗下所有的小节 - 批量删除
     * 删除小节的时候，也要删除其对应的视频
     *
     * @param courseId
     */
    @Override
    public boolean removeVideoByCourseId(String courseId) {
        // 根据课程id查询出全部的小节
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId); // 该课程下的全部小节
        queryWrapper.select("video_source_id"); // 只查询出视频id
        List<EduVideo> eduVideos = baseMapper.selectList(queryWrapper); // 根据课程id查询该课程下的全部的小节
        List<String> deleteVideos = new ArrayList<>();
        for (EduVideo eduVideo : eduVideos) { // 收集小节下的全部视频id
            String videoSourceId = eduVideo.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)) {
                deleteVideos.add(videoSourceId);
            }
        }
        if (deleteVideos.size() > 0) { // 进行批量删除小节的视频
            vodClient.deleteBatch(deleteVideos);
        }

        // 删除相关的video小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        Integer count = baseMapper.delete(wrapper);// 删除全部小节
        return count != null && count > 0;
    }
}
