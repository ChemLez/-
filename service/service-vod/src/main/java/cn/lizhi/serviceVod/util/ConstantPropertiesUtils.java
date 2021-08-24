package cn.lizhi.serviceVod.util;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.vod.file")
@Data
public class ConstantPropertiesUtils implements InitializingBean {


    //    @Value("${aliyun.vod.file.keyid}")
    private String keyId;

    //    @Value("${aliyun.vod.file.keysecret}")
    private String keySecret;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;

    public ConstantPropertiesUtils() {
        System.out.println("...");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        ACCESS_KEY_ID = this.keyId;
        ACCESS_KEY_SECRET = this.keySecret;
    }
}
