package cn.lizhi.serviceAcl.service;

import cn.lizhi.serviceAcl.entity.AclPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
public interface AclPermissionService extends IService<AclPermission> {

    // 获取全部菜单
    List<AclPermission> queryAllMenu();

    // 删除全部菜单
    void removeChildrenPermission(String id);

    void savePermission(AclPermission permission);

    void assignPermission2Role(String roleId, String[] permissionId);

    List<AclPermission> selectAllMenu(String roleId);
}
