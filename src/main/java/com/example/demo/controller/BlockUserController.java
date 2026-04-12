package com.example.demo.controller;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlockUserController {
    @Autowired
    private UserRepository repo;

    @GetMapping("/user/block-user")
    public String getAll(Model model){
        model.addAttribute("listBlock", repo.getAllBlockUser());
        return "User/block-user";
    }
}
