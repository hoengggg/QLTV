package com.example.demo.controller;

import com.example.demo.repository.ThongKeHoatDongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongKeHoatDongController {
    @Autowired
    private ThongKeHoatDongRepository repo;

    @GetMapping("/thong-ke-hoat-dong")
    public String getAll(Model model){
        model.addAttribute("listThongKe", repo.getAllThongKeHoatDong());
        return "User/thong-ke-hoat-dong";
    }
}
