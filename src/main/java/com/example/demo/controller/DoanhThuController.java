package com.example.demo.controller;

import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoanhThuController {
    @Autowired
    private PaymentRepository repo;

    @GetMapping("/payment/doanh-thu")
    public String getAll(Model model){
        model.addAttribute("listDoanhThu", repo.getAllDoanhThu());
        return "Payment/doanh-thu";
    }
}
