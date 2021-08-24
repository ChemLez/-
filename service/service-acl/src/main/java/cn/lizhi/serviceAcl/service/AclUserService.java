package cn.lizhi.serviceAcl.service;

import cn.lizhi.serviceAcl.entity.AclUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
public interface AclUserService extends IService<AclUser> {

    void indexQuery(Page<AclUser> pageParam, AclUser userQueryVo);

}
