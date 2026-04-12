package com.example.demo.controller;

import com.example.demo.repository.MembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberShipActiveController {
    @Autowired
    private MembershipRepository repo;

    @GetMapping("/member/member-active")
    public String getAll(Model model){
        model.addAttribute("listMember", repo.getAllMember());
        return "Membership/list-member-active";
    }
}
