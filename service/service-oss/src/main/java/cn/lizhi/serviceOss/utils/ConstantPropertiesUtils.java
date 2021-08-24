package cn.lizhi.serviceOss.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * OSS参数工具类
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean { // 对Bean对象创建完成以后，进行初始化工作


    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.file.keyid}")
    private String keyId;
    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;
    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;


    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception { // bean的初始化操作
        END_POINT = this.endpoint;
        KEY_ID = this.keyId;
        KEY_SECRET = this.keySecret;
        BUCKET_NAME = this.bucketName;
    }


}
