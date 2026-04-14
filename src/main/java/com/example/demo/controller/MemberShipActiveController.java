package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.MembershipRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberShipActiveController {
    @Autowired
    private MembershipRepository repo;

    @GetMapping("/member/member-active")
    public String getAll(Model model, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName) && !"Librarian".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

        model.addAttribute("listMember", repo.getAllMember());
        return "Membership/list-member-active";
    }
}
