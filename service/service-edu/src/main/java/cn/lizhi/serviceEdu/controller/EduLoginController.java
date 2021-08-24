package cn.lizhi.serviceEdu.controller;

import cn.lizhi.commonUtils.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serviceEdu/user")
@Api(tags = {"后台登录"})
@CrossOrigin // 解决跨域问题
public class EduLoginController {


    @PostMapping("login")
    public Result login() {
        return Result.ok().data("token","admin");
    }

    @GetMapping("info")
    public Result info() {
        return Result.ok().data("roles", "[admin]").data("name", "admin").data("avatar", "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3351113425,3755484207&fm=26&gp=0.jpg");
    }


}
