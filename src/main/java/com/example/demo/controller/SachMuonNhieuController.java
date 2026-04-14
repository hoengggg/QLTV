package com.example.demo.controller;

import com.example.demo.repository.SachMuonNhieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SachMuonNhieuController {
    @Autowired
    private SachMuonNhieuRepository repo;

    @GetMapping("/sach-muon-nhieu")
    public String getAll(Model model){
        model.addAttribute("listSachMuonNhieu", repo.getAllSachMuonNhieu());
        return "Book/sach-muon-nhieu";
    }
}
