package cn.lizhi.serviceCMS.service;

import cn.lizhi.serviceCMS.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author liz
 * @since 2021-08-17
 */
public interface CrmBannerService extends IService<CrmBanner> {

    CrmBanner getBannerById(String id);

    void saveBanner(CrmBanner banner);

    void updateBannerById(CrmBanner banner);

    void removeBannerById(String id);

    void pageBanner(Page<CrmBanner> pageParam, Object o);

    List<CrmBanner> selectIndexList();
}
