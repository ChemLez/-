package cn.lizhi.serviceOrder.client;

import cn.lizhi.commonUtils.orderVo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    @PostMapping("/serviceUcenter/ucenter-member/getUserInfoOrder/{id}")
    UcenterMemberOrder getUcenterMemberById(@PathVariable("id") String id);

}
