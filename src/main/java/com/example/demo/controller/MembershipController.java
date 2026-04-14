package com.example.demo.controller;

import com.example.demo.model.Membership;
import com.example.demo.model.User;
import com.example.demo.repository.MembershipRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MembershipController {
    @Autowired
    private MembershipRepository repoMember;

    @GetMapping("/membership")
    public String getAll(Model model, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName) && !"Librarian".equalsIgnoreCase(roleName) && !"Student".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

        model.addAttribute("listMembership", repoMember.findAll());
        return "Membership/membership_hien_thi";
    }

    @GetMapping("/membership/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoMember.deleteById(id);
        return "redirect:/membership";
    }

    @GetMapping("/membership/add")
    public String addView(Model model){
        model.addAttribute("newMembership", new Membership());
        return "Membership/membership_add";
    }

    @PostMapping("/membership/add")
    public String add(Membership membership){
        repoMember.save(membership);
        return "redirect:/membership";
    }

    @GetMapping("/membership/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Member_Tim", repoMember.findById(id));
        return "Membership/membership_detail";
    }

    @PostMapping("/membership/update")
    public String update(@ModelAttribute("Member_Tim") Membership membership){
        repoMember.save(membership);
        return "redirect:/membership";
    }

    @GetMapping("/membership/search-range")
    public String searchRange(
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end, Model model) {
        List<Membership> list = repoMember.findByStartDateBetween(start, end);
        model.addAttribute("listMembership", list);
        if (list.isEmpty()) model.addAttribute("message", "Không có thành viên đăng ký trong khoảng này!");
        return "Membership/membership_hien_thi";
    }
}
