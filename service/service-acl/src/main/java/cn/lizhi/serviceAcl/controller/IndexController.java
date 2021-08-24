package cn.lizhi.serviceAcl.controller;

import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceAcl.service.IndexService;
import io.swagger.annotations.Api;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/acl/index")
@Api(tags = "前台首页登录")
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 根据token获取用户信息
     * @return
     */
//    @GetMapping("info")
//    public Result info() {
//        // 获取当前登录用户的用户名
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Map<String, Object> map = indexService.getUserInfo(username);
//        return Result.ok().data(map);
//    }


}
