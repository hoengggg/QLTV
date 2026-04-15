package com.example.demo.controller;

import com.example.demo.model.Payment;
import com.example.demo.model.User;
import com.example.demo.repository.FineRepository;
import com.example.demo.repository.PaymentRepository;
import jakarta.servlet.http.HttpSession;
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
    public String getAll(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName) && !"Librarian".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

        int pageIndex = (page < 1) ? 0 : page - 1;

        Page<Payment> pageData = repoPayment.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("method", null);
        model.addAttribute("listPayment", repoPayment.findAll(PageRequest.of(pageIndex, 10)));
        model.addAttribute("method23", null);
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
    public String filterPayment(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Chuyển đổi trang UI (1, 2...) sang index code (0, 1...)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // Thực hiện truy vấn có phân trang
        Page<Payment> pageData = repoPayment.findByAmountBetween(min, max, PageRequest.of(pageIndex, 10));

        // Đưa dữ liệu sang HTML
        model.addAttribute("pageData", pageData);
        model.addAttribute("listPayment", pageData.getContent());

        // Lưu lại min/max để các nút chuyển trang giữ được điều kiện lọc
        model.addAttribute("minSearch", min);
        model.addAttribute("maxSearch", max);

        if (pageData.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy giao dịch thanh toán nào trong khoảng này!");
        }

        return "Payment/payment_hien_thi";
    }
}
