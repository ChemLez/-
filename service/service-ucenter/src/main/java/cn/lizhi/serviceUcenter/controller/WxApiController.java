package cn.lizhi.serviceUcenter.controller;

import cn.lizhi.commonUtils.JwtUtils;
import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceUcenter.entity.UcenterMember;
import cn.lizhi.serviceUcenter.service.UcenterMemberService;
import cn.lizhi.util.ConstantPropertiesUtil;
import cn.lizhi.util.HttpClientUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 微信登录
 */
@Controller
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
@Api(tags = {"微信登录接口"})
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 便于本地处理的回调地址
     * 获取到微信返回的code，再带着appId和secret进行访问
     *
     * @return
     */
    @GetMapping("callback")
    public String callBack(String code, String state) {

        UcenterMember member = memberService.callBack(code, state); // 回调 获取用户信息
        //使用jwt根据member对象生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        //最后：返回首页面，通过路径传递token字符串
        return "redirect:http://localhost:3000?token=" + jwtToken;

    }

    /**
     * 生成微信二维码
     * 参照微信的开发文档，请求api，带上参数
     * @return
     */
    @GetMapping("login")
    public String getWxCode() {

        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect?" +
                "appid=%s&" +
                "redirect_uri=%s&" +
                "response_type=code&" +
                "scope=snsapi_login&" +
                "state=%s#wechat_redirect";

        // 对redirect_url 进行编码
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format(baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu");

        // 重定向到微信开放的api
        return "redirect:" + url;
    }

}
