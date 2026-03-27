package com.example.demo.controller;

import com.example.demo.model.Fine;
import com.example.demo.repository.FineRepository;
import com.example.demo.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FineController {
    @Autowired
    private FineRepository repoFine;

    @Autowired
    private LoanRepository repoLoan;

    @GetMapping("/fine")
    public String getAll(Model model){
        model.addAttribute("listFine", repoFine.findAll());
        return "Fine/fine_hien_thi";
    }

    @GetMapping("/fine/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoFine.deleteById(id);
        return "redirect:/fine";
    }

    @GetMapping("/fine/add")
    public String addView(Model model){
        model.addAttribute("newFine", new Fine());
        model.addAttribute("listLoan", repoLoan.findAll());
        return "Fine/fine_add";
    }

    @PostMapping("/fine/add")
    public String add(Fine fine, Model model){
        model.addAttribute("listLoan", repoLoan.findAll());
        repoFine.save(fine);
        return "redirect:/fine";
    }

    @GetMapping("/fine/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Fine_Tim", repoFine.findById(id));
        model.addAttribute("listLoan", repoLoan.findAll());
        return "Fine/fine_detail";
    }

    @PostMapping("/fine/update")
    public String update(@ModelAttribute("Fine_Tim") Fine fine, Model model){
        model.addAttribute("listLoan", repoLoan.findAll());
        repoFine.save(fine);
        return "redirect:/fine";
    }

    @GetMapping("/fine/loc")
    public String filterFine(@RequestParam("min") Double min,
                             @RequestParam("max") Double max, Model model) {
        model.addAttribute("listFine", repoFine.findByAmountBetween(min, max));
        return "Fine/fine_hien_thi";
    }
}
