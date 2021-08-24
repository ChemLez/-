package cn.lizhi.serviceAcl.controller;


import cn.lizhi.commonUtils.MD5;
import cn.lizhi.commonUtils.Result;
import cn.lizhi.serviceAcl.entity.AclUser;
import cn.lizhi.serviceAcl.service.AclRoleService;
import cn.lizhi.serviceAcl.service.AclUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author liz
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/admin/acl/user")
public class AclUserController {


    @Autowired
    private AclUserService userService;

    @Autowired
    private AclRoleService roleService;


    @ApiOperation(value = "获取管理用户分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    AclUser userQueryVo) {

        Page<AclUser> pageParam = new Page<>(page, limit);
        userService.indexQuery(pageParam, userQueryVo);
        return Result.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
    }

    @ApiOperation(value = "新增管理用户")
    @PostMapping("save")
    public Result save(@RequestBody AclUser user) {
        user.setPassword(MD5.encrypt(user.getPassword()));
        userService.save(user);
        return Result.ok();
    }

    @ApiOperation(value = "修改管理用户")
    @PutMapping("update")
    public Result updateById(@RequestBody AclUser user) {
        userService.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除管理用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        userService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据id列表删除管理用户")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<String> idList) {
        userService.removeByIds(idList);
        return Result.ok();
    }

    @ApiOperation(value = "根据用户获取role角色相关数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable String userId) {
        Map<String, Object> roleMap = roleService.findRoleByUserId(userId);
        return Result.ok().data(roleMap);
    }

    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam String userId,@RequestParam String[] roleId) {
        roleService.saveUserRoleRealtionShip(userId,roleId);
        return Result.ok();
    }

}

