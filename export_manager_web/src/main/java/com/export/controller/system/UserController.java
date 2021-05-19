package com.export.controller.system;

import com.alibaba.druid.util.StringUtils;
import com.export.domain.system.Dept;
import com.export.domain.system.User;
import com.export.service.system.DeptService;
import com.export.service.system.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @RequestMapping("list")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        String companyId = "1";
        PageInfo<User> pageInfo = userService.findPageByCompanyId(companyId, page, size);
        model.addAttribute("pageInfo", pageInfo);
        return "/system/user/user-list";
    }

    @RequestMapping("toAdd")
    public String toAdd(Model model){
        setDeptList(model);
        return "system/user/user-add";
    }

    @RequestMapping(value="/edit" ,name="保存用户方法")
    public String edit(User user){
 /* `create_by` varchar(40) DEFAULT NULL COMMENT '登录人编号',
  `create_dept` varchar(40) DEFAULT NULL COMMENT '登录人所属部门编号',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(40) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,*/

//        user的id如果为空就是新增，反之就是修改
        if(StringUtils.isEmpty(user.getId())){ //id为空
            //        新增时需要赋id
            user.setCompanyId(getCompanyId());
            user.setCompanyName(getCompanyName());
            user.setCreateTime(new Date());
            user.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id
            userService.save(user);
        }else {  //id不为空
            user.setUpdateTime(new Date());
            user.setCompanyId(getCompanyId());
            userService.update(user);
        }

        return "redirect:/system/user/list.do";  //重定向到列表数据
    }

    @RequestMapping("toUpdate")
    public String toUpdate(Model model, String id){
        User user = userService.getById(id);
        model.addAttribute("user", user);
        setDeptList(model);
        return "system/user/user-update";
    }

    @RequestMapping("changeRole")
    public String changeRole(String[] roleIds, String userid){
        userService.saveRoleUser(roleIds, userid);
        return "redirect:/system/user/list.do";
    }

    private String getCompanyId(){
        return "1";
    }

    private String getCompanyName(){
        return "侯氏企业";
    }

    private void setDeptList(Model model){
        List<Dept> list = deptService.findByCompanyIdNew(getCompanyId());
        model.addAttribute("deptList", list);
    }
}
