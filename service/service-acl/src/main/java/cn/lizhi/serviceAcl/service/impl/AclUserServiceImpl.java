package cn.lizhi.serviceAcl.service.impl;

import cn.lizhi.serviceAcl.entity.AclUser;
import cn.lizhi.serviceAcl.mapper.AclUserMapper;
import cn.lizhi.serviceAcl.service.AclUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
@Service
public class AclUserServiceImpl extends ServiceImpl<AclUserMapper, AclUser> implements AclUserService {

    @Override
    public void indexQuery(Page<AclUser> pageParam, AclUser userQueryVo) {
        QueryWrapper<AclUser> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(userQueryVo.getUsername())) {
            wrapper.like("username",userQueryVo.getUsername());
        }
        IPage<AclUser> pageModel = baseMapper.selectPage(pageParam, wrapper);
    }
}
