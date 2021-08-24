package cn.lizhi.serviceOrder.client;

import cn.lizhi.commonUtils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-edu") // 指定需要调用的模块
public interface EduClient {

    @GetMapping("/serviceEdu/edu-course/{courseId}")
    Result getCourseInfo(@PathVariable("courseId") String courseId);


}
