package com.example.demo.controller;

import com.example.demo.model.Loan;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class LoanController {
    @Autowired
    private LoanRepository repoLoan;

    @Autowired
    private UserRepository repoUser;

    @GetMapping("/loan")
    public String getAll(Model model){
        model.addAttribute("listLoan", repoLoan.findAll());
        return "Loan/loan_hien_thi";
    }

    @GetMapping("/loan/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoLoan.deleteById(id);
        return "redirect:/loan";
    }

    @GetMapping("/loan/add")
    public String addView(Model model){
        model.addAttribute("newLoan", new Loan());
        model.addAttribute("listStatus", repoLoan.findDistinctStatusBy());
        model.addAttribute("listUser", repoUser.findAll());
        return "Loan/loan_add";
    }

    @PostMapping("/loan/add")
    public String add(Loan loan, Model model){
        model.addAttribute("listStatus", repoLoan.findDistinctStatusBy());
        model.addAttribute("listUser", repoUser.findAll());
        repoLoan.save(loan);
        return "redirect:/loan";
    }

    @GetMapping("/loan/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Loan_Tim", repoLoan.findById(id));
        model.addAttribute("listStatus", repoLoan.findDistinctStatusBy());
        model.addAttribute("listUser", repoUser.findAll());
        return "Loan/loan_detail";
    }

    @PostMapping("/loan/update")
    public String update(@ModelAttribute("Loan_Tim") Loan loan, Model model){
        model.addAttribute("listStatus", repoLoan.findDistinctStatusBy());
        model.addAttribute("listUser", repoUser.findAll());
        repoLoan.save(loan);
        return "redirect:/loan";
    }

    @GetMapping("/loan/search")
    public String search(@RequestParam("status") String status, Model model){
        model.addAttribute("listLoan", repoLoan.findByStatusContaining(status));
        return "Loan/loan_hien_thi";
    }

    @GetMapping("/loan/loc")
    public String filterLoan(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                             @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                             Model model) {
        model.addAttribute("listLoan", repoLoan.findByLoanDateBetween(start, end));
        return "Loan/loan_hien_thi";
    }
}
