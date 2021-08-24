package cn.lizhi.serviceCMS.controller;


import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceCMS.entity.CrmBanner;
import cn.lizhi.serviceCMS.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-08-17
 */
@RestController
@RequestMapping("/educms/crmBanner")
@CrossOrigin
@Api(tags = {"后台banner的相关操作"})
public class CrmBannerController {


    @Autowired
    private CrmBannerService bannerService;


    /**
     * 分页查询
     * @param page 当前页数
     * @param limit 每页的记录数
     * @return
     */
    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("find/{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<CrmBanner> pageParam = new Page<>(page, limit);
        bannerService.pageBanner(pageParam, null);
        // 返回当前页的记录内容，总记录数
        return Result.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());

    }

    /**
     * 根据id查询Banner
     *
     * @param id：Banner id
     * @return
     */
    @GetMapping("find/{id}")
    public Result get(@PathVariable String id) {
        CrmBanner banner = bannerService.getBannerById(id);
        return Result.ok().data("item", banner);
    }

    /**
     * 保存banner
     *
     * @param banner
     * @return
     */
    @PostMapping
    public Result save(@RequestBody CrmBanner banner) {
        bannerService.saveBanner(banner);
        return Result.ok();
    }

    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public Result updateById(@RequestBody CrmBanner banner) {
        bannerService.updateBannerById(banner);
        return Result.ok();
    }

    @ApiOperation(value = "删除Banner")
    @DeleteMapping("delete/{id}")
    public Result remove(@PathVariable String id) {
        bannerService.removeBannerById(id);
        return Result.ok();
    }

}