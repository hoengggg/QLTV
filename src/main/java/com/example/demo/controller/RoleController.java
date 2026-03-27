package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleRepository repoRole;

    @GetMapping("/role")
    public String getAll(Model model){
        model.addAttribute("listRole", repoRole.findAll());
        return "Role/role_hien_thi";
    }

    @GetMapping("/role/add")
    public String addView(Model model){
        model.addAttribute("newRole", new Role());
        return "Role/role_add";
    }

    @PostMapping("/role/add")
    public String add(Role role){
        repoRole.save(role);
        return "redirect:/role";
    }

    @GetMapping("/role/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Role_Tim", repoRole.findById(id).orElse(null));
        return "Role/role_detail";
    }

    @PostMapping("/role/update")
    public String update(@ModelAttribute("Role_Tim") Role role){
        repoRole.save(role);
        return "redirect:/role";
    }

    @GetMapping("/role/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoRole.deleteById(id);
        return "redirect:/role";
    }

    @GetMapping("/role/search-range")
    public String searchRange(@RequestParam("min") Integer min, @RequestParam("max") Integer max, Model model) {
        List<Role> list = repoRole.findByPermissionLevelBetween(min, max);
        model.addAttribute("listRole", list);
        if (list.isEmpty()) model.addAttribute("message", "Cấp độ quyền hạn không tồn tại!");
        return "Role/role_hien_thi";
    }
}
