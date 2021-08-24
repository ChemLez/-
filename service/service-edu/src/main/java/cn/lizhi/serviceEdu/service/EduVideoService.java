package cn.lizhi.serviceEdu.service;

import cn.lizhi.serviceEdu.entity.EduVideo;
import cn.lizhi.serviceEdu.vo.video.VideoInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author liz
 * @since 2021-03-01
 */
public interface EduVideoService extends IService<EduVideo> {

    boolean getCountByChapterId(String id);

    void saveVideoInfo(VideoInfoVo videoInfoVo);

    EduVideo getVideoInfoById(String id);

    void updateVideoInfoById(VideoInfoVo videoInfoVo);

    boolean removeVideoById(String id);

    boolean removeVideoByCourseId(String courseId);
}
