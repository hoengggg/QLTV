package com.example.demo.controller;

import com.example.demo.model.Fine;
import com.example.demo.model.User;
import com.example.demo.repository.FineRepository;
import com.example.demo.repository.LoanRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public String getAll(Model model, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName) && !"Librarian".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

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
    public String filterFine(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Xử lý index trang (Trang 1 hiển thị = index 0)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // Truy vấn có phân trang (ví dụ 10 bản ghi mỗi trang)
        Page<Fine> pageData = repoFine.findByAmountBetween(min, max, PageRequest.of(pageIndex, 10));

        // Đưa dữ liệu sang HTML
        model.addAttribute("pageData", pageData);
        model.addAttribute("listFine", pageData.getContent());

        // Gửi lại giá trị để giữ trạng thái lọc trên thanh URL của các nút chuyển trang
        model.addAttribute("minSearch", min);
        model.addAttribute("maxSearch", max);

        if (pageData.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy khoản phạt nào trong khoảng này!");
        }

        return "Fine/fine_hien_thi";
    }
}
