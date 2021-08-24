package cn.lizhi.serviceAcl.controller;


import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceAcl.entity.AclPermission;
import cn.lizhi.serviceAcl.service.AclPermissionService;
import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.dc.path.PathError;

import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/admin/acl/permission")
@CrossOrigin
@Api(tags = {"Permission接口"})
public class AclPermissionController {

    @Autowired
    private AclPermissionService permissionService;

    /**
     * 查询所有菜单
     * @return
     */
    @GetMapping("findAll")
    @ApiOperation(value = "查询出全部的权限列表")
    public Result indexAllPermission() {

        List<AclPermission> list = permissionService.queryAllMenu();
        return Result.ok().data("item", list);
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        permissionService.removeChildrenPermission(id);
        return Result.ok();
    }

    /**
     * 新增菜单
     * @param permission
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "新增权限菜单")
    public Result save(@RequestBody AclPermission permission) {
        permissionService.savePermission(permission);
        return Result.ok();
    }

    /**
     * 修改菜单
     * @param permission
     * @return
     */
    @PutMapping("update")
    @ApiOperation(value = "修改权限菜单")
    public Result updateById(@RequestBody AclPermission permission) {
        permissionService.updateById(permission);
        return Result.ok();
    }

    /**
     * 给角色分配权限
     * 新增权限角色时，为这个角色赋予权限
     * @param roleId
     * @param permissionId
     * @return
     */
    @PostMapping("assign")
    @ApiOperation(value = "分配权限给角色")
    public Result doAssign(String roleId, String[] permissionId) {
        permissionService.assignPermission2Role(roleId, permissionId);
        return Result.ok();
    }

    /**
     * 根据角色获取菜单
     * @param roleId
     * @return
     */
    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("assign/{roleId}")
    public Result toAssign(@PathVariable String roleId) {
        List<AclPermission> list = permissionService.selectAllMenu(roleId);
        return Result.ok().data("children", list);
    }


}

