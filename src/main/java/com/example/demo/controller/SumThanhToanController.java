package com.example.demo.controller;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SumThanhToanController {
    @Autowired
    private UserRepository repo;

    @GetMapping("/user/thanh-toan")
    public String getAll(Model model){
        model.addAttribute("list_thanh_toan", repo.getAllThanhToan());
        return "User/tong-tien-thanh-toan";
    }
}
