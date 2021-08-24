package cn.lizhi.serviceEdu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-order")
public interface OrderClient {

    //查询订单信息
    @GetMapping("/serviceOrder/t-order/isBuyCourse/{memberId}/{orderId}")
    public boolean isBuyCourse(@PathVariable("memberId") String memberid,
                               @PathVariable("orderId") String id);

}
