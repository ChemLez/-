package cn.lizhi.serviceUcenter.service;

import cn.lizhi.serviceUcenter.entity.UcenterMember;
import cn.lizhi.serviceUcenter.vo.LoginVo;
import cn.lizhi.serviceUcenter.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author liz
 * @since 2021-08-18
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(LoginVo loginVo);

    void register(RegisterVo registerVo);

    LoginVo getLoginInfo(String memberId);

    UcenterMember getOpenIdMember(String openid);

    UcenterMember callBack(String code, String state);
}
