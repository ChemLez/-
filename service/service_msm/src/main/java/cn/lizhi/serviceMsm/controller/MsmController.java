package cn.lizhi.serviceMsm.controller;

import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceMsm.service.MsmService;
import cn.lizhi.serviceMsm.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/serviceMsm/msm")
@CrossOrigin
public class MsmController {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MsmService msmService;


    @GetMapping("/send/{phone}")
    public Result code(@PathVariable String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) return Result.ok(); // 表明验证码已经发送过了，redis中存在了验证码
        // 获取不到 通过阿里云进行发送
        code = RandomUtils.getFourBitRandom(); // 随机生成四位的验证码
        Map<String, Object> param = new HashMap<>();
        param.put("code", code); // 存放的验证码
        boolean isSend = msmService.send(phone, param);
        if (isSend) {
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES); // 将手机 - 验证码 键值对格式 设置到redis中，并设置过期时间为五分钟
            return Result.ok();
        }
        return Result.error().message("短信发送失败");
    }
}
