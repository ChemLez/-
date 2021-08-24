package cn.lizhi.serviceEdu.client;

import cn.lizhi.commonUtils.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 采用熔断机制中的回调函数
 */
@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public Result removeVideo(String videoId) {
        return Result.error().message("time out");
    }

    @Override
    public Result deleteBatch(List<String> videoIds) {
        return Result.error().message("time out");
    }
}
