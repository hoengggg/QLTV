package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.MembershipRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public String getAll(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

        int pageIndex = (page < 1) ? 0 : page - 1;

        Page<User> pageData = repoUser.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("name", null);
        model.addAttribute("listUser", repoUser.findAll(PageRequest.of(pageIndex, 10)));
        model.addAttribute("name23", null);
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
    public String search(@RequestParam("name") String name, @RequestParam(defaultValue = "0") int page, Model model){
        int pageIndex = (page < 1) ? 0 : page - 1;

        String searchKW = (name != null) ? name.trim() : "";

        Page<User> pageData = repoUser.findByNameContaining(searchKW, PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("listUser", pageData.getContent());
        model.addAttribute("name23", searchKW);
        return "User/user_hien_thi";
    }

    @GetMapping("/user/search-range")
    public String searchRange(@RequestParam("min") Double min,
                              @RequestParam("max") Double max,
                              Model model) {
        // 1. Lấy danh sách từ Repo
        List<User> list = repoUser.findByPenaltyBalanceBetween(min, max);

        // 2. Tạo một Page "giả" để Thymeleaf không bị lỗi null totalPages
        // Bạn dùng PageImpl để bọc cái list lại
        org.springframework.data.domain.Page<User> pageData =
                new org.springframework.data.domain.PageImpl<>(list);

        // 3. Đẩy CẢ HAI vào model
        model.addAttribute("pageData", pageData); // Dòng này giúp hết lỗi 500
        model.addAttribute("listUser", list);

        if (list.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy người dùng nào!");
        }
        return "User/user_hien_thi";
    }
}
