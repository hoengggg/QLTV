package com.example.demo.controller;

import com.example.demo.model.Reservation;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public String getAll(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName) && !"Librarian".equalsIgnoreCase(roleName) && !"Student".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

        int pageIndex = (page < 1) ? 0 : page - 1;

        Page<Reservation> pageData = repoReservation.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("status", null);
        model.addAttribute("listReservation", repoReservation.findAll(PageRequest.of(pageIndex, 10)));
        model.addAttribute("status23", null);
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
    public String search(@RequestParam("status") String status, @RequestParam(defaultValue = "0") int page, Model model){
        int pageIndex = (page < 1) ? 0 : page - 1;

        String searchKW = (status != null) ? status.trim() : "";

        Page<Reservation> pageData = repoReservation.findByStatusContaining(searchKW, PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("listReservation", pageData.getContent());
        model.addAttribute("status23", searchKW);
        return "Reservation/reservation_hien_thi";
    }
}
