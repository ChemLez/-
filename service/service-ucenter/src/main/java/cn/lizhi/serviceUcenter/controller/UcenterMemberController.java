package cn.lizhi.serviceUcenter.controller;


import cn.lizhi.commonUtils.JwtUtils;
import cn.lizhi.commonUtils.Result;
import cn.lizhi.commonUtils.orderVo.CourseWebVoOrder;
import cn.lizhi.commonUtils.orderVo.UcenterMemberOrder;
import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceUcenter.entity.UcenterMember;
import cn.lizhi.serviceUcenter.service.UcenterMemberService;
import cn.lizhi.serviceUcenter.vo.LoginVo;
import cn.lizhi.serviceUcenter.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * 登录/注册/token获取信息
 * </p>
 *
 * @author liz
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/serviceUcenter/ucenter-member")
@Api(tags = {"注册登录模块"})
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 表单登录
     * @param loginVo
     * @return
     */
    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo); // 登录用户
        return Result.ok().data("token", token); // 用户登录成功后，返回token给客户端
    }


    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public Result register(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return Result.ok();
    }

    /**
     * 进行token信息验证
     * @param request
     * @return
     */
    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("auth/getLoginInfo")
    public Result getLoginInfo(HttpServletRequest request){
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request); // 获取到token中保留的id
            LoginVo loginInfoVo = memberService.getLoginInfo(memberId);
            return Result.ok().data("item", loginInfoVo);
        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"error");
        }
    }

    /**
     * 根据token字符串获取用户信息
     * @param id
     * @return
     */
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = memberService.getById(id); // 查询出用户信息
        UcenterMemberOrder uMember = new UcenterMemberOrder();
        BeanUtils.copyProperties(member, uMember);
        return uMember;
    }

}

