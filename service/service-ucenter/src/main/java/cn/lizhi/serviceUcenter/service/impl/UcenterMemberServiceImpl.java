package cn.lizhi.serviceUcenter.service.impl;

import cn.lizhi.commonUtils.JwtUtils;
import cn.lizhi.commonUtils.MD5;
import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceUcenter.entity.UcenterMember;
import cn.lizhi.serviceUcenter.mapper.UcenterMemberMapper;
import cn.lizhi.serviceUcenter.service.UcenterMemberService;
import cn.lizhi.serviceUcenter.vo.LoginVo;
import cn.lizhi.serviceUcenter.vo.RegisterVo;
import cn.lizhi.util.ConstantPropertiesUtil;
import cn.lizhi.util.HttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-08-18
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 会员登录校验
     *
     * @param loginVo: 登录信息
     * @return
     */
    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数
        if (StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "error");
        }

        //获取会员 - 根据手机号查询会员
        UcenterMember member = baseMapper.selectOne(new
                QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (null == member) {
            throw new GuliException(20001, "error");
        }

        // 校验密码
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(20001, "error");
        }

        //校验是否被禁用
        if (member.getIsDisabled()) {
            throw new GuliException(20001, "error");
        }

        //使用JWT生成token字符串
        String token = JwtUtils.getJwtToken(member.getId(), member.getNickname()); // token生成的规则中含有 id和NickName
        return token;

    }

    /**
     * 注册接口
     * 先对注册信息进行校验
     *
     * @param registerVo
     */
    @Override
    public void register(RegisterVo registerVo) {

        // 获取注册信息，对注册信息进行校验
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        // 校验参数 - 是否为空
        //校验参数
        if (StringUtils.isEmpty(mobile) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(code)) {
            throw new GuliException(20001, "error");
        }

        // 校验验证码 - 因为在缓存中，所以先校验缓存中的参数
        String mobleCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(mobleCode)) { // 获取到的验证码不正确
            throw new GuliException(20001, "error");
        }

        // 查询数据库中是否存在相同的手机号码 - 校验数据库中的数据唯一性
        Integer count = baseMapper.selectCount(new
                QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (count > 0) {
            throw new GuliException(20001, "error");
        }

        //添加注册信息到数据库
        UcenterMember member = new UcenterMember();
        member.setNickname(nickname);
        member.setMobile(registerVo.getMobile());
        member.setPassword(MD5.encrypt(password)); // 采用MD5进行加密
        member.setIsDisabled(false);

        member.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");

        this.save(member);

    }

    /**
     * 根据token获取到登录的信息
     *
     * @param memberId
     * @return
     */
    @Override
    public LoginVo getLoginInfo(String memberId) {
        UcenterMember member = baseMapper.selectById(memberId);
        LoginVo loginVo = new LoginVo();
        BeanUtils.copyProperties(member, loginVo);
        return loginVo;
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(queryWrapper);
        return member;
    }

    @Override
    public UcenterMember callBack(String code, String state) {
        try {
            // 地址请求
            // 根据返回的code 带上验证 再次请求开放api
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            // 参数拼接
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantPropertiesUtil.WX_OPEN_APP_ID,
                    ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                    code);
            String accessTokenInfo = null; // 微信平台返回的token
            try {
                accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 返回accessToken - 对token进行解析
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token"); // 获取access_token
            String openid = (String) mapAccessToken.get("openid");

            // 首先通过openid 判断用户是否在库中，如果已经存在该信息，就直接登录即可
            UcenterMember member = getOpenIdMember(openid);
            if (member == null) { // 表明用户是首次登录，将该用户信息添加到数据库中

                // 同构accessTokenId和openid 再去请求微信提供固定地址，获取扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";

                // 拼接参数
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                // 发送请求
                String userInfo = null;
                try {
                    userInfo = HttpClientUtils.get(userInfoUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class); // 对用户信息字符串进行解析
                // 从map中获取用户的信息
                String nickname = (String) userInfoMap.get("nickname");
                String headimgurl = (String) userInfoMap.get("headimgurl");
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                member.setNickname(nickname);
                baseMapper.insert(member);
            }
            return member;
        } catch (GuliException e) {
            throw new GuliException(20001, "login error");
        }

    }
}
