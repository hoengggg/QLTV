package com.example.demo.controller;

import com.example.demo.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SachQuaHanTraController {
    @Autowired
    private LoanRepository repo;

    @GetMapping("/loan/sach-qua-han")
    public String getAll(Model model){
        model.addAttribute("sachQuaHan", repo.getAllSachQuaHan());
        return "Loan/sach-qua-han";
    }
}
