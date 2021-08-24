package cn.lizhi.serviceEdu.client;

import cn.lizhi.commonUtils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-vod", fallback = VodFileDegradeFeignClient.class)
@Component
public interface VodClient {

    @DeleteMapping("/serviceVod/vod/video/{videoId}")
    Result removeVideo(@PathVariable("videoId") String videoId); // 远程调用的接口

    @DeleteMapping("/serviceVod/vod/video/delete-batch")
    Result deleteBatch(@RequestParam("videoIdList") List<String> videoIds);
}
