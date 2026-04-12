package com.example.demo.controller;

import com.example.demo.model.Payment;
import com.example.demo.repository.FineRepository;
import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaymentController {
    @Autowired
    private PaymentRepository repoPayment;

    @Autowired
    private FineRepository repoFine;

    @GetMapping("/payment")
    public String getAll(Model model, @RequestParam(defaultValue = "0") int page){
        int pageIndex = (page < 1) ? 0 : page - 1;

        Page<Payment> pageData = repoPayment.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("method", null);
        model.addAttribute("listPayment", repoPayment.findAll(PageRequest.of(pageIndex, 10)));
        return "Payment/payment_hien_thi";
    }

    @GetMapping("/payment/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoPayment.deleteById(id);
        return "redirect:/payment";
    }

    @GetMapping("/payment/add")
    public String addView(Model model){
        model.addAttribute("newPayment", new Payment());
        model.addAttribute("listMethod", repoPayment.findDistinctMethodBy());
        model.addAttribute("listStatus", repoPayment.findDistinctStatusBy());
        model.addAttribute("listFine", repoFine.findAll());
        return "Payment/payment_add";
    }

    @PostMapping("/payment/add")
    public String add(Model model, Payment payment){
        model.addAttribute("listMethod", repoPayment.findDistinctMethodBy());
        model.addAttribute("listStatus", repoPayment.findDistinctStatusBy());
        model.addAttribute("listFine", repoFine.findAll());
        repoPayment.save(payment);
        return "redirect:/payment";
    }

    @GetMapping("/payment/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Payment_Tim", repoPayment.findById(id));
        model.addAttribute("listMethod", repoPayment.findDistinctMethodBy());
        model.addAttribute("listStatus", repoPayment.findDistinctStatusBy());
        model.addAttribute("listFine", repoFine.findAll());
        return "Payment/payment_detail";
    }

    @PostMapping("/payment/update")
    public String update(@ModelAttribute("Payment_Tim") Payment payment, Model model){
        model.addAttribute("listMethod", repoPayment.findDistinctMethodBy());
        model.addAttribute("listStatus", repoPayment.findDistinctStatusBy());
        model.addAttribute("listFine", repoFine.findAll());
        repoPayment.save(payment);
        return "redirect:/payment";
    }

    @GetMapping("/payment/search")
    public String search(@RequestParam("method") String method, @RequestParam(defaultValue = "0") int page, Model model){
        int pageIndex = (page < 1) ? 0 : page - 1;

        String searchKW = (method != null) ? method.trim() : "";

        Page<Payment> pageData = repoPayment.findByMethodContaining(searchKW, PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("listPayment", pageData.getContent());
        model.addAttribute("method23", searchKW);

        return "Payment/payment_hien_thi";
    }

    @GetMapping("/payment/loc")
    public String filterFine(@RequestParam("min") Double min,
                             @RequestParam("max") Double max, Model model) {
        model.addAttribute("listPayment", repoPayment.findByAmountBetween(min, max));
        return "Payment/payment_hien_thi";
    }
}
