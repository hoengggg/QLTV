package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository repoUser;

    @Autowired
    private RoleRepository repoRole;

    @Autowired
    private MembershipRepository repoMembership;

    @GetMapping("/user")
    public String getAll(Model model){
        model.addAttribute("listUser", repoUser.findAll());
        return "User/user_hien_thi";
    }

    @GetMapping("/user/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoUser.deleteById(id);
        return "redirect:/user";
    }

    @GetMapping("/user/add")
    public String addView(Model model){
        model.addAttribute("newUser", new User());
        model.addAttribute("listRole", repoRole.findAll());
        model.addAttribute("listMembership", repoMembership.findAll());
        return "User/user_add";
    }

    @PostMapping("/user/add")
    public String add(User user, Model model){
        model.addAttribute("listRole", repoRole.findAll());
        model.addAttribute("listMembership", repoMembership.findAll());
        repoUser.save(user);
        return "redirect:/user";
    }

    @GetMapping("/user/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("User_Tim", repoUser.findById(id));
        model.addAttribute("listRole", repoRole.findAll());
        model.addAttribute("listMembership", repoMembership.findAll());
        return "User/user_detail";
    }

    @PostMapping("/user/update")
    public String update(@ModelAttribute("User_Tim") Model model, User user){
        repoUser.save(user);
        return "redirect:/user";
    }

    @GetMapping("/user/search")
    public String search(@RequestParam("name") String name, Model model){
        model.addAttribute("listUser", repoUser.findByNameContaining(name));
        return "User/user_hien_thi";
    }

    @GetMapping("/user/search-range")
    public String searchRange(@RequestParam("min") Double min, @RequestParam("max") Double max, Model model) {
        List<User> list = repoUser.findByPenaltyBalanceBetween(min, max);
        model.addAttribute("listUser", list);
        if (list.isEmpty()) model.addAttribute("message", "Không tìm thấy người dùng nào trong khoảng phạt này!");
        return "User/user_hien_thi";
    }
}
