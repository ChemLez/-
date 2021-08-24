package cn.lizhi.serviceOss.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * OssService接口
 */
public interface OssService {

    String uploadFileAvatar(MultipartFile file) throws IOException;
}
