package cn.lizhi.serviceVod.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    /**
     * 文件上传接口
     * @param file:待上传的文件
     * @return
     */
    String uploadVideo(MultipartFile file);

    void removeVideo(String videoId);

    void removeMoreAlyVideo(List<String> videoIdList);

}
