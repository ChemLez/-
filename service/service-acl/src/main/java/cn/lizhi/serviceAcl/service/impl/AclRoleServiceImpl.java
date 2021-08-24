package cn.lizhi.serviceAcl.service.impl;

import cn.lizhi.serviceAcl.entity.AclRole;
import cn.lizhi.serviceAcl.entity.AclUserRole;
import cn.lizhi.serviceAcl.mapper.AclRoleMapper;
import cn.lizhi.serviceAcl.service.AclRoleService;
import cn.lizhi.serviceAcl.service.AclUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
@Service
public class AclRoleServiceImpl extends ServiceImpl<AclRoleMapper, AclRole> implements AclRoleService {

    @Autowired
    private AclUserRoleService userRoleService; // 中间表

    // 分页查询
    @Override
    public void selectIndex(Page<AclRole> pageParam) {
        baseMapper.selectPage(pageParam, null);
    }

    /**
     * 根据用户id 获取用户数据；即获取：
     * 1.全部的roles
     * 2.该用户分配到的roles
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> findRoleByUserId(String userId) {

        List<AclRole> assignRoles = new ArrayList<>();

        QueryWrapper<AclUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).select("role_id"); // 只查出role_id字段 -> 对应的权限role
        List<AclUserRole> userRoles = userRoleService.list(wrapper); // 该用户-权限角色关系

        // 存在的角色的权限用户
        List<String> existRoleList = userRoles.stream().map(AclUserRole::getRoleId).collect(Collectors.toList()); // 序列化

        // 查询出全部的role
        List<AclRole> allRoles = baseMapper.selectList(null); // 全部的roles
        for (AclRole role : allRoles) {
            if (existRoleList.contains(role.getId())) { // 表明权限已经分配给了这个用户
                assignRoles.add(role);
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put("assignRoles", assignRoles);
        res.put("allRoles", allRoles);
        return res;
    }

    /**
     * 保存用户和权限角色之间的关系
     * 一个user分配多个role权限 user-role 关系保存到第三张表中
     * @param userId
     * @param roleId
     */
    @Override
    public void saveUserRoleRealtionShip(String userId, String[] roleId) {
        List<AclUserRole> insertList = new ArrayList<>();
        for (String rid : roleId) {
            if (StringUtils.isEmpty(rid)) continue;
            AclUserRole aclUserRole = new AclUserRole();
            aclUserRole.setUserId(userId);
            aclUserRole.setRoleId(rid);
        }
        userRoleService.saveBatch(insertList);
    }
}
