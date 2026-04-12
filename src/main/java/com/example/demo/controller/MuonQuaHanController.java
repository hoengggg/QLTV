package com.example.demo.controller;

import com.example.demo.dto.MuonQuaHanDto;
import com.example.demo.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MuonQuaHanController {
    @Autowired
    private LoanRepository repo;

    @GetMapping("/loan/muon-qua-han")
    public String getAll(Model model){
        model.addAttribute("loan", repo.getAllMuonQuaHan());
        return "Loan/loan-muon-qua-han";
    }
}
