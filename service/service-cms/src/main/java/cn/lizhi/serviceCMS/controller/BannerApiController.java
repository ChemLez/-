package cn.lizhi.serviceCMS.controller;

import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceCMS.entity.CrmBanner;
import cn.lizhi.serviceCMS.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/educms/banner")
@Api(tags = "网站首页Banner列表")
@CrossOrigin //跨域
public class BannerApiController {

    @Autowired
    private CrmBannerService bannerService;

    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public Result index() {
        List<CrmBanner> list = bannerService.selectIndexList();
        return Result.ok().data("bannerList", list);
    }

}
