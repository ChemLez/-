package cn.lizhi.serviceAcl.service.impl;

import cn.lizhi.serviceAcl.service.AclUserService;
import cn.lizhi.serviceAcl.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {


    @Autowired
    private AclUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();

        return null;

    }
}
