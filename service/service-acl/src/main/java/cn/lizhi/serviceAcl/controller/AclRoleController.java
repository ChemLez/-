package cn.lizhi.serviceAcl.controller;


import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceAcl.entity.AclRole;
import cn.lizhi.serviceAcl.service.AclRoleService;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.security.acl.Acl;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/admin/acl/role")
public class AclRoleController {

    @Autowired
    private AclRoleService roleService;

    /**
     * 权限角色的分页查询
     * 代码编写同讲师列表查询
     *
     * @param current
     * @param limit
     * @return
     */
    @GetMapping("index/{current}/{limit}")
    @ApiOperation(value = "")
    public Result index(@PathVariable Long current, @PathVariable Long limit) {
        Page<AclRole> pageParam = new Page(current, limit);
        roleService.selectIndex(pageParam);
        return Result.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("get/{id}")
    public Result get(@PathVariable String id) {
        AclRole role = roleService.getById(id);
        return Result.ok().data("item", role);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public Result save(@RequestBody AclRole role) {
        roleService.save(role);
        return Result.ok();
    }

    @ApiOperation(value = "修改角色")
    @PutMapping("update")
    public Result updateById(@RequestBody AclRole role) {
        roleService.updateById(role);
        return Result.ok();
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        roleService.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除
     * @param idList
     * @return
     */
    @ApiOperation(value = "根据id列表删除角色")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList) {
        roleService.removeByIds(idList);
        return Result.ok();
    }

}

