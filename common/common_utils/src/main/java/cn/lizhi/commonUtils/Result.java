package cn.lizhi.commonUtils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    @ApiModelProperty(value = "是否成功")
    private Boolean isSuccess;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>(); // 用来设定返回数据,value为Object类型，即为任意数据类型

    private Result() { // 构造方法私有化，只能在该类方法创建，不能在外部使用new关键字创建该对象

    }

    // 返回成功 状态的 Result对象
    public static Result ok() {
        Result result = new Result();
        result.setIsSuccess(true); // 设置为true
        result.setCode(ResultCode.SUCCESS);  // code
        result.setMessage("成功"); // message
        return result;
    }

    // 返回失败 状态的 Result对象
    public static Result error() {
        Result result = new Result();
        result.setIsSuccess(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("失败");
        return result;
    }

    // 以下目的为 细化信息, 核心：为了能够链式编程

    // 返回失败/成功 状态的 Result对象
    public Result isSuccess(Boolean isSuccess) { // 自定义设定 true/false
        this.setIsSuccess(isSuccess);
        return this;
    }

    public Result message(String message) { // 设定自定义四信息 - message
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code) { // 设定自定义状态码
        this.setCode(code);
        return this;
    }

    // 直接存入对象
    public Result data(String name, Object object) { // 设置模型的数据
        this.data.put(name, object);
        return this;
    }

    // 设置map集合
    public Result data(Map<String, Object> map) { // 直接设定模型
        this.setData(map);
        return this;
    }

}
