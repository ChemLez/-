package cn.lizhi.security.security;

import cn.lizhi.commonUtils.ResponseUtil;
import cn.lizhi.commonUtils.Result;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 登出业务逻辑类
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("token");
        if (token != null) {
            tokenManager.removeToken(token); // 移除token

            //清空当前用户缓存中的权限数据
            String userName = tokenManager.getUserFromToken(token); // 从token中获取userName
            redisTemplate.delete(userName); // 从redis中删除token
        }
        ResponseUtil.out(response, Result.ok());
    }

}