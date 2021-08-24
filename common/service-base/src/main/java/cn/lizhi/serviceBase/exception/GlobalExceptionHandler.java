package cn.lizhi.serviceBase.exception;

import cn.lizhi.commonUtils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设置该类的目的，方便对异常的统一处理，其返回结果都是同一个结果
 * 统一异常处理类
 */
@ControllerAdvice // 控制器增强
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class) // 异常处理器 所有的异常处理器
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error();
    }

    // 特定异常,可以用来自定义异常  java.lang.ArithmeticException: / by zero
    @ExceptionHandler(ArithmeticException.class) // 定义处理的异常类型，为计算数据的类型
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace(); // 打印异常信息
        return Result.error().message("执行了自定义异常...");
    }

    // 当出现该异常时的统一返回结果
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public Result error(GuliException e) {
        e.printStackTrace();
        return Result.error().message(e.getMsg());
    }




}
