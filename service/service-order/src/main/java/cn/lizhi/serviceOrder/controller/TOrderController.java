package cn.lizhi.serviceOrder.controller;


import cn.lizhi.commonUtils.JwtUtils;
import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceOrder.entity.TOrder;
import cn.lizhi.serviceOrder.service.TOrderService;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/serviceOrder/t-order")
@Api(tags = {"订单详情"})
public class TOrderController {

    @Autowired
    @Qualifier("orderService")
    private TOrderService orderService;


    /**
     * 生成订单接口
     * @param courseId
     * @param request
     * @return 返回订单号
     */
    @PostMapping("createOrder/{courseId}")
    public Result saveOrder(@PathVariable String courseId, HttpServletRequest request) {

        // 创建订单
        String orderNo = orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request)); // 返回订单号
        return Result.ok().data("orderId", orderNo);
    }

    /**
     * 根据订单号 获取订单
     * @param orderId 订单号
     * @return
     */
    @GetMapping("getOrder/{orderId}")
    public Result getOrder(@PathVariable String orderId) {
        TOrder order = orderService.getOrderByOrderId(orderId);
        return Result.ok().data("item", order);
    }

    /**
     * 前台课程展示：课程是否免费/购买
     * 根据用户Id和订单orderId 查询用户对应的该课程是否支付成功
     * 用于用户当前课程是否就有观看权
     * @param memberId
     * @param orderId
     * @return
     */
    @GetMapping("isBuyCourse/{memberId}/{orderId}")
    public Result isBuyCourse(@PathVariable String memberId, @PathVariable String orderId) {

        boolean res = orderService.isByCourse(memberId, orderId);
        if (!res) {
            return Result.error().message("课程未购买");
        }

        return Result.ok().message("已购买");
    }


}

