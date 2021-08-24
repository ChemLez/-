package cn.lizhi.serviceUcenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "登录对象", description = "登录对象表单")
@Data
public class LoginVo {

    @ApiModelProperty(value = "手机号")
    private String mobile;


    @ApiModelProperty(value = "登录密码")
    private String password;
}
