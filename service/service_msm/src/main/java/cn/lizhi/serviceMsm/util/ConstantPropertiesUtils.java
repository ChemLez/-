package cn.lizhi.serviceMsm.util;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * OSS参数工具类
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean { // 对Bean对象创建完成以后，进行初始化工作


    @Value("${aliyun.msm.file.sys.domain}")
    private String domain;

    @Value("${aliyun.msm.file.sys.version}")
    private String version;

    @Value("${aliyun.msm.file.sys.action}")
    private String action;

    @Value("${aliyun.msm.file.sys.region}")
    private String region;

    @Value("${aliyun.msm.file.sys.keyid}")
    private String keyId;

    @Value("${aliyun.msm.file.sys.keysecret}")
    private String keySecret;

    @Value("${aliyun.msm.file.query.signName}")
    private String signName;

    @Value("${aliyun.msm.file.query.templateCode}")
    private String templateCode;


    public static String MSM_DOMAIN;
    public static String MSM_VERSION;
    public static String MSM_ACTION;
    public static String MSM_REGION;
    public static String MSM_KEYID;
    public static String MSM_KEYSECRET;
    public static String MSM_SINGNAME;
    public static String MSM_TEMPLATECODE;


    @Override
    public void afterPropertiesSet() throws Exception { // bean的初始化操作
        MSM_DOMAIN = this.domain;
        MSM_VERSION = this.version;
        MSM_ACTION = this.action;
        MSM_REGION = this.region;
        MSM_KEYID = this.keyId;
        MSM_KEYSECRET = this.keySecret;
        MSM_TEMPLATECODE = this.templateCode;
        MSM_SINGNAME = this.signName;
    }


}
