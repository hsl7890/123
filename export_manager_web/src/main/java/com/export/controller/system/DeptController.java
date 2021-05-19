package com.export.controller.system;

import com.export.domain.system.Dept;
import com.export.service.system.DeptService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("system/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @RequestMapping("list")
    public String list(Model model, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        String companyId = "1";
        PageInfo<Dept> pageInfo = deptService.findByCompanyId(companyId, page, size);
        model.addAttribute("pageInfo", pageInfo);
        return "/system/dept/dept-list";
    }
}
