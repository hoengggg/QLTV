package com.example.demo.controller;

import com.example.demo.model.Reservation;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservationController {
    @Autowired
    private ReservationRepository repoReservation;

    @Autowired
    private UserRepository repoUser; // Để lấy danh sách User cho combobox

    @Autowired
    private BookRepository repoBook; // Để lấy danh sách Book cho combobox

    @GetMapping("/reservation")
    public String getAll(Model model){
        model.addAttribute("listReservation", repoReservation.findAll());
        return "Reservation/reservation_hien_thi";
    }

    @GetMapping("/reservation/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoReservation.deleteById(id);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/add")
    public String addView(Model model){
        model.addAttribute("newReservation", new Reservation());
        model.addAttribute("listUser", repoUser.findAll());
        model.addAttribute("listBook", repoBook.findAll());
        return "Reservation/reservation_add";
    }

    @PostMapping("/reservation/add")
    public String add(Reservation reservation){
        repoReservation.save(reservation);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Reservation_Tim", repoReservation.findById(id).get());
        model.addAttribute("listUser", repoUser.findAll());
        model.addAttribute("listBook", repoBook.findAll());
        return "Reservation/reservation_detail";
    }

    @PostMapping("/reservation/update")
    public String update(@ModelAttribute("Reservation_Tim") Reservation reservation){
        repoReservation.save(reservation);
        return "redirect:/reservation";
    }

    @GetMapping("/reservation/search")
    public String search(@RequestParam("status") String status, Model model){
        model.addAttribute("listReservation", repoReservation.findByStatusContaining(status));
        return "Reservation/reservation_hien_thi";
    }
}
