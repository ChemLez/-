package cn.lizhi.serviceAcl.service.impl;

import cn.lizhi.serviceAcl.entity.AclPermission;
import cn.lizhi.serviceAcl.entity.AclRolePermission;
import cn.lizhi.serviceAcl.mapper.AclPermissionMapper;
import cn.lizhi.serviceAcl.service.AclPermissionService;
import cn.lizhi.serviceAcl.service.AclRolePermissionService;
import cn.lizhi.serviceBase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.BiIntFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.internal.$Gson$Types;
import org.apache.poi.sl.draw.geom.Guide;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
@Service
public class AclPermissionServiceImpl extends ServiceImpl<AclPermissionMapper, AclPermission> implements AclPermissionService {


    @Autowired
    private AclRolePermissionService rolePermissionService;

    /**
     * 查询出全部的菜单
     *
     * @return
     */
    @Override
    public List<AclPermission> queryAllMenu() {

        // 首先查询出全部的菜单权限
        QueryWrapper<AclPermission> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("CAST(pid AS SIGNED)");
        List<AclPermission> res = new ArrayList<>();
        List<AclPermission> permissions = baseMapper.selectList(wrapper);
        if (permissions.get(0).getPid().equals("0")) { // 根目录菜单
            res.add(permissions.get(0));
            getChildrenPermission(permissions, res.get(0), 1);
        }
        return res;
    }

    /**
     * 递归删除菜单 核心在于 id = pid
     *
     * @param id
     */
    @Override
    public void removeChildrenPermission(String id) {

        try {
            QueryWrapper<AclPermission> wrapper = new QueryWrapper<>();
            wrapper.select("id", "pid"); // 只需要查询出这两个字段即可
            List<Integer> deleteIds = new ArrayList<>(); // 需要删除的id
            List<AclPermission> permissions = baseMapper.selectList(wrapper); // 查询出所有的菜单列表信息
            for (AclPermission permission : permissions) {
                if (permission.getId().equals(id)) {
                    recursiveDelete(permissions, deleteIds, permission);
                }
            }
            baseMapper.deleteBatchIds(deleteIds);
        } catch (Exception e) {
            throw new GuliException(20001, "权限菜单删除失败");
        }

    }

    @Override
    public void savePermission(AclPermission permission) {
        int insert = baseMapper.insert(permission);
        if (insert == 0) {
            throw new GuliException(20001, "新增失败");
        }
    }

    @Override
    public void assignPermission2Role(String roleId, String[] permissionIds) {
        try {
            List<AclRolePermission> res = new ArrayList<>();
            for (String permissionId : permissionIds) { // 权限不为空
                if (!StringUtils.isEmpty(permissionId)) {
                    AclRolePermission aclRolePermission = new AclRolePermission();
                    aclRolePermission.setRoleId(roleId);
                    aclRolePermission.setPermissionId(permissionId);
                    res.add(aclRolePermission);
                }
            }
            rolePermissionService.saveBatch(res);
        } catch (Exception e) {
            throw new GuliException(20001, "角色权限赋予失败");
        }

    }

    /**
     * 根据角色获取权限菜单
     *
     * @param roleId
     * @return
     */
    @Override
    public List<AclPermission> selectAllMenu(String roleId) {

        // 查询出全部的权限的列表
        List<AclPermission> allPermissionList = baseMapper.selectList(new QueryWrapper<AclPermission>().orderByAsc("CAST(id AS SIGNED)"));

        // 根据用户id - role_id 查询出全部的 role_permission 条目
        List<AclRolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<AclRolePermission>().eq("role_id", roleId));

        List<AclPermission> currentPermission2Role = new ArrayList<>();
        for (AclPermission aclPermission : allPermissionList) {
            for (AclRolePermission aclRolePermission : rolePermissionList) {
                if (aclPermission.getId().equals(aclRolePermission.getPermissionId())) { // 当前的权限是属于该用户的
                    aclPermission.setSelect(true);
                    currentPermission2Role.add(aclPermission);
                    break;
                }
            }
        }
        getChildrenPermission(currentPermission2Role, currentPermission2Role.get(0), 1);
        List<AclPermission> rr = new ArrayList<>();
        rr.add(currentPermission2Role.get(0));
        return rr;
    }

    /**
     * 递归查找所有需要删除的菜单项
     *
     * @param allPermission
     * @param deleteIds
     * @param currentPermission
     */
    private void recursiveDelete(List<AclPermission> allPermission, List<Integer> deleteIds, AclPermission currentPermission) {
        deleteIds.add(Integer.parseInt(currentPermission.getId()));
        for (AclPermission aclPermission : allPermission) {
            if (aclPermission.getPid().equals(currentPermission.getId())) {
                recursiveDelete(allPermission, deleteIds, aclPermission);
            }
        }
    }

    /**
     * 递归的获取子列表
     *
     * @param permissions
     */
    private void getChildrenPermission(List<AclPermission> permissions, AclPermission currentPermission, int level) {

        List<AclPermission> list = new ArrayList<>(); // 每一层的children列表

        currentPermission.setLevel(level);
        for (AclPermission permission : permissions) {
            if (permission.getPid().equals(currentPermission.getId())) {
                if (currentPermission.getChildren() == null) {
                    list.add(permission);
                    currentPermission.setChildren(list);
                } else {
                    currentPermission.getChildren().add(permission);
                }
                getChildrenPermission(permissions, permission, level + 1);
            }
        }

    }
}
