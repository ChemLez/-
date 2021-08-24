package cn.lizhi.serviceOrder.controller;


import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceOrder.mapper.TPayLogMapper;
import cn.lizhi.serviceOrder.service.TPayLogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/serviceOrder/t-pay-log")
@Api(tags = "订单支付日志流水信息")
public class TPayLogController {

    @Autowired
    @Qualifier("payLogService")
    private TPayLogService payService;

    /**
     * 根据订单id生成二维码
     * @param orderNo
     * @return
     */
    @GetMapping("/createNative/{orderNo}")
    public Result createNative(@PathVariable String orderNo) {
        Map map = payService.createNative(orderNo);
        return Result.ok().data(map); // 直接将map设置到result中

    }

    /**
     * 获取支付状态接口
     * @param orderNo
     * @return
     */
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable String orderNo) {
        Map<String,String> map = payService.queryPayStatus(orderNo);
        if (map == null) { // 说明不存在订单
            return Result.error().message("支付出错");
        }

        if (map.get("trade_state").equals("SUCCESS")) { // 支付成功
            payService.updateOrderStatus(map); // 更改订单状态
            return Result.ok().message("支付成功");
        }

        return Result.ok().code(25000).message("支付中");
    }


}

