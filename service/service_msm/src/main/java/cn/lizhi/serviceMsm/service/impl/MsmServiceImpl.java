package cn.lizhi.serviceMsm.service.impl;

import cn.lizhi.serviceMsm.service.MsmService;
import cn.lizhi.serviceMsm.util.ConstantPropertiesUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {

    /**
     * 发送短信的服务
     *
     * @param phoneNumbers
     * @param param
     * @return
     */
    @Override
    public boolean send(String phoneNumbers,  Map<String, Object> param) {

        if (StringUtils.isEmpty(phoneNumbers)) { // 号码为空或者不存在
            return false;
        }

        DefaultProfile profile = DefaultProfile.getProfile(ConstantPropertiesUtils.MSM_REGION, ConstantPropertiesUtils.MSM_KEYID, ConstantPropertiesUtils.MSM_KEYSECRET);

        IAcsClient client = new DefaultAcsClient(profile);


        CommonRequest request = new CommonRequest();
        // 设置的固定参数
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(ConstantPropertiesUtils.MSM_DOMAIN);
        request.setSysVersion(ConstantPropertiesUtils.MSM_VERSION);
        request.setSysAction(ConstantPropertiesUtils.MSM_ACTION);

        // 设置发送相关的参数
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", ConstantPropertiesUtils.MSM_SINGNAME);
        request.putQueryParameter("TemplateCode", ConstantPropertiesUtils.MSM_TEMPLATECODE);

        // request.putQueryParameter("TemplateParam", "{\"code\":\"1111\"}"); 可以产出验证码格式为 {code:xxxx} ，需要需要将Map进行序列化
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); // 验证码数据，转换成json数据

        try {
            // 进行发送
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
