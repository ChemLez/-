package cn.lizhi.serviceAcl.service;

import cn.lizhi.serviceAcl.entity.AclRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
public interface AclRoleService extends IService<AclRole> {

    void selectIndex(Page<AclRole> pageParam);

    Map<String, Object> findRoleByUserId(String userId);

    void saveUserRoleRealtionShip(String userId, String[] roleId);
}
