package cn.lizhi.serviceCMS.service.impl;

import cn.lizhi.serviceBase.exception.GuliException;
import cn.lizhi.serviceCMS.entity.CrmBanner;
import cn.lizhi.serviceCMS.mapper.CrmBannerMapper;
import cn.lizhi.serviceCMS.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-08-17
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Cacheable(value = "banner", key = "#id") // 组成在redis中的key -> banner::selectIndexList
    @Override
    public CrmBanner getBannerById(String id) {
        CrmBanner banner = baseMapper.selectById(id);
        if (banner == null) {
            throw new GuliException(20001, "查询失败，未查询到该信息");
        }
        return banner;
    }

    @Override
//    @CachePut(value = "banner",key = "#banner.id")
    public void saveBanner(CrmBanner banner) {
        Integer count = baseMapper.insert(banner);
        if (count == null || count == 0) {
            throw new GuliException(20001, "查询失败，未查询到该信息");
        }
    }

    @CacheEvict(value = "banner",allEntries = true)
    @Override
    public void updateBannerById(CrmBanner banner) {

        Integer count = baseMapper.updateById(banner);
        if (count == null || count == 0) {
            throw new GuliException(20001, "查询失败，未查询到该信息");
        }
    }

    @Override
    @CacheEvict(value = "banner",key = "#id")
    public void removeBannerById(String id) {

        Integer count = baseMapper.deleteById(id);
        if (count == null || count == 0) {
            throw new GuliException(20001, "查询失败，未查询到该信息");
        }
    }

    @Override
    public void pageBanner(Page<CrmBanner> pageParam, Object pageQuery) {
        baseMapper.selectPage(pageParam, null);
    }

    /**
     * get index_page
     * @return
     */
    @Override
    @Cacheable(value = "banner", key = "'selectIndexList'") // 组成在redis中的key -> banner::selectIndexList
    public List<CrmBanner> selectIndexList() {
        List<CrmBanner> crmBanners = baseMapper.selectList(null);
        return crmBanners;
    }
}
