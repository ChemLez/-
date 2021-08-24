package cn.lizhi.serviceVod.service.impl;

import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceVod.service.VideoService;
import cn.lizhi.serviceVod.util.AliyunVodSDKUtils;
import cn.lizhi.serviceVod.util.ConstantPropertiesUtils;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Override
    public String uploadVideo(MultipartFile file) {
        try {
            InputStream in = file.getInputStream(); // 获取文件输入流
            String originalFilename = file.getOriginalFilename(); // 上传时候的文件名 filename.视频格式
            assert originalFilename != null;
            String title = originalFilename.substring(0, originalFilename.lastIndexOf(".")); // 文件名
            UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtils.ACCESS_KEY_ID,
                    ConstantPropertiesUtils.ACCESS_KEY_SECRET,
                    title,
                    originalFilename, in); // 采用的网络流的request

            UploadVideoImpl upload = new UploadVideoImpl(); // 视频上传对象
            UploadStreamResponse response = upload.uploadStream(request); // 视频上传

            String videoId = response.getVideoId();
            if (!response.isSuccess()) { // 上传失败
                String errorMessage = "阿里云上传错误：" + "code：" +
                        response.getCode() + ", message：" + response.getMessage(); // 可能出现的错误信息，不一定是上传失败的错误信息

                if (StringUtils.isEmpty(videoId)) { // 如果 视频id不存在则为是失败
                    throw new GuliException(20001, errorMessage);
                }
            }
            return videoId;
        } catch (IOException e) {
            throw new GuliException(20001, "guli vod 服务上传失败");
        }
    }

    /**
     * 根据视频id删除视频
     *
     * @param videoId
     */
    @Override
    public void removeVideo(String videoId) {

        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtils.ACCESS_KEY_ID,
                    ConstantPropertiesUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest(); // 创建删除视频request对象
            request.setVideoIds(videoId); // 设置视频id
            DeleteVideoResponse response = client.getAcsResponse(request); // 调用api方法进行删除
        } catch (ClientException e) {
            throw new GuliException(20001, "视频删除失败");
        }

    }

    /**
     * 批量删除多个视频
     * @param videoIdList: 需要批量删除的视频id
     */
    @Override
    public void removeMoreAlyVideo(List<String> videoIdList) {

        if (videoIdList.size() > 0) {
            try {
                DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                        ConstantPropertiesUtils.ACCESS_KEY_ID,
                        ConstantPropertiesUtils.ACCESS_KEY_SECRET);
                DeleteVideoRequest request = new DeleteVideoRequest();
                //支持传入多个视频ID，多个用逗号分隔
                String videoIds = org.apache.commons.lang.StringUtils.join(videoIdList, ",");
                request.setVideoIds(videoIds);
                client.getAcsResponse(request);
            } catch (ClientException e) {
                e.printStackTrace();
            }

        }
    }
}
