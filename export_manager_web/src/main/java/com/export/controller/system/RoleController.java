package com.export.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.export.domain.system.Module;
import com.export.domain.system.Role;
import com.export.domain.system.User;
import com.export.service.system.ModuleService;
import com.export.service.system.RoleService;
import com.export.service.system.UserService;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping("list")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        PageInfo<Role> pageInfo = roleService.findByPage(getCompanyId(),page, size);
        model.addAttribute("page", pageInfo);
        return "/system/role/role-list";
    }

    @RequestMapping("toAdd")
    public String toAdd(Model model){
//        this.setMenu(model);
        return "system/role/role-add";
    }

    @RequestMapping("edit")
    public String edit(Role role){
        role.setCompanyId(getCompanyId());
        if(StringUtils.isEmpty(role.getId())){
            role.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id
            roleService.save(role);
        }else{
            roleService.update(role);
        }
        return "redirect:/system/role/list.do";
    }

    @RequestMapping("roleUser")
    public String roleUser(String userId, Model model){
        // 根据用户Id查询这个用户拥有的角色
        List<Role> userRoleList = roleService.findRoleByUserId(userId);
        StringBuffer userRoleStr = new StringBuffer();
        for (Role role : userRoleList) {
            userRoleStr.append(role.getId() + ",");
        }

        // 查询所有角色
        List<Role> list = roleService.findAll(getCompanyId());
        User user = userService.getById(userId);
        // 将当前传入的用户所拥有的角色放入model中
        model.addAttribute("userRoleStr", userRoleStr);
        // 将所有角色放入model中
        model.addAttribute("roleList", list);
        // 将当前用户放入model中
        model.addAttribute("user", user);
        return "system/user/user-role";
    }

    @RequestMapping("toUpdate")
    public String toUpdate(String id, Model model){
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
//        this.setMenu(model);
        return "system/role/role-update";
    }

    @RequestMapping("roleModule")
    public String roleModule(String roleid, Model model){
        Role role = roleService.getById(roleid);
        model.addAttribute("role", role);
        return "system/role/role-module";

    }

    @RequestMapping("initModuleData")
    @ResponseBody
    public List initModuleData(String id){
        List<Module> modules = moduleService.findAll();
        List<Module> roleModules = moduleService.findRoleModuleByRoleId(id);
        //2.构造map集合
        List list = new ArrayList();
        //构造map
        for (Module module : modules) {  //循环所有的模块
            //初始化map
            Map map = new HashMap<>();
            //添加map中的数据
            map.put("id",module.getId());   //模块id
            map.put("pId",module.getParentId());  //父模块id
            map.put("name",module.getName()); //模板名称

            if(roleModules.contains(module)) {
                map.put("checked",true); //默认勾选
            }
            //存入list集合
            list.add(map);
        }
        return list;
    }

    @RequestMapping("updateRoleModule")
    public String updateRoleModule(String roleid, String moduleIds){
        moduleService.saveRoleModule(roleid, moduleIds);
        return "redirect:/system/role/list.do";
    }

//    private void setMenu(Model model){
//        List<Role> roleList = roleService.findAll();
//        model.addAttribute("menus",roleList);
//    }
    private String getCompanyId(){
    return "1a71b18c-d902-41d3-aafd-83ea8c205bd3";
}
}
