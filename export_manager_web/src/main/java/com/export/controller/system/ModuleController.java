package com.export.controller.system;

import com.export.domain.system.Module;
import com.export.service.system.ModuleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("system/module")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @RequestMapping("list")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        PageInfo<Module> pageInfo = moduleService.findByPage(page, size);
        model.addAttribute("page", pageInfo);
        return "/system/module/module-list";
    }

    @RequestMapping("toAdd")
    public String toAdd(Model model){
        this.setMenu(model);
        return "system/module/module-add";
    }

    @RequestMapping("edit")
    public String edit(Module module){
        if(StringUtils.isEmpty(module.getId())){
            module.setId(UUID.randomUUID().toString());  //id 赋值成一个随机id
            moduleService.save(module);
        }else{
            moduleService.update(module);
        }
        return "redirect:/system/module/list.do";
    }

    @RequestMapping("toUpdate")
    public String toUpdate(String id, Model model){
        Module module = moduleService.getById(id);
        model.addAttribute("module", module);
        this.setMenu(model);
        return "system/module/module-update";
    }

    private void setMenu(Model model){
        List<Module> moduleList = moduleService.findAll();
        model.addAttribute("menus",moduleList);
    }
}
