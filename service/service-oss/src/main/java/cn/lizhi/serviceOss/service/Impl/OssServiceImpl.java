package cn.lizhi.serviceOss.service.Impl;

import cn.lizhi.serviceOss.service.OssService;
import cn.lizhi.serviceOss.utils.ConstantPropertiesUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) throws IOException {

        // Endpoint以杭州为例，其它Region请按实际情况填写。 通过配置类的方式进行编写，而不是直接将参数值写入进行。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        String dataPath = new DateTime().toString("yyyy/MM/dd"); // 设定时间模板
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret); // OSS 客户端实例
        String fileName = dataPath + "/" + UUID.randomUUID().toString().replaceAll("-", "") + file.getOriginalFilename(); // 文件名
        // 创建PutObjectRequest对象。其中参数 key 为 文件名称,File对象可以获取文件相关 信息
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file.getInputStream());

        // 上传文件 - 客户端上传文件
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
        String url = "https://" + bucketName + "." + endpoint + "/" + fileName; // 生成的oss文件存储的文件名
        return url;

    }
}
