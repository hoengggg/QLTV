package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Loan;
import com.example.demo.model.Loan_detail;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanDetailRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class LoanController {
    @Autowired
    private LoanRepository repoLoan;

    @Autowired
    private UserRepository repoUser;

    @Autowired
    private BookRepository repoBook;

    @Autowired
    private LoanDetailRepository repoLoanDetail; // Thêm dòng này để điều khiển bảng trung gian

    @GetMapping("/loan")
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

        Page<Loan> pageData = repoLoan.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("status", null);
        model.addAttribute("listLoan", repoLoan.findAll(PageRequest.of(pageIndex, 10)));
        model.addAttribute("status23", null);
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
        model.addAttribute("listBook", repoBook.findAll()); // Để hiển thị CBB Book
        return "Loan/loan_add";
    }

    @PostMapping("/loan/add")
    public String add(Loan loan, @RequestParam("bookId") Long bookId){
        // 1. Lưu Loan trước
        Loan savedLoan = repoLoan.save(loan);

        // 2. Tạo chi tiết phiếu mượn để lưu sách đã chọn
        Loan_detail detail = new Loan_detail();
        detail.setLoan(savedLoan);
        detail.setBook(repoBook.findById(bookId).orElse(null));
        detail.setQuantity(1); // Mặc định là 1 cuốn

        repoLoanDetail.save(detail); // Quan trọng nhất: Lưu vào bảng trung gian

        return "redirect:/loan";
    }

    @GetMapping("/loan/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        Loan loan = repoLoan.findById(id).orElse(null);
        model.addAttribute("Loan_Tim", loan);
        model.addAttribute("listStatus", repoLoan.findDistinctStatusBy());
        model.addAttribute("listUser", repoUser.findAll());
        model.addAttribute("listBook", repoBook.findAll()); // Để hiện CBB Book

        // Tìm ID sách hiện tại đang mượn để HTML hiển thị đúng
        if (loan != null && !loan.getLoan_details().isEmpty()) {
            model.addAttribute("currentBookId", loan.getLoan_details().get(0).getBook().getId());
        }

        return "Loan/loan_detail";
    }

    @PostMapping("/loan/update")
    public String update(@ModelAttribute("Loan_Tim") Loan loan, @RequestParam("bookId") Long bookId){
        // 1. Cập nhật bảng Loan
        repoLoan.save(loan);

        // 2. Cập nhật lại sách trong bảng Loan_detail
        List<Loan_detail> details = repoLoanDetail.findByLoanId(loan.getId());
        if(!details.isEmpty()){
            Loan_detail detail = details.get(0);
            detail.setBook(repoBook.findById(bookId).orElse(null));
            repoLoanDetail.save(detail);
        }

        return "redirect:/loan";
    }

    @GetMapping("/loan/search")
    public String search(@RequestParam("status") String status, @RequestParam(defaultValue = "0") int page, Model model){
        int pageIndex = (page < 1) ? 0 : page - 1;

        String searchKW = (status != null) ? status.trim() : "";

        Page<Loan> pageData = repoLoan.findByStatusContaining(searchKW, PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("listLoan", pageData.getContent());
        model.addAttribute("status23", searchKW);

        return "Loan/loan_hien_thi";
    }

    @GetMapping("/loan/loc")
    public String filterLoan(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Xử lý index trang (Trang 1 trên web = index 0 trong DB)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // Truy vấn phân trang (mỗi trang 10 bản ghi)
        Page<Loan> pageData = repoLoan.findByLoanDateBetween(start, end, PageRequest.of(pageIndex, 10));

        // Đưa dữ liệu sang HTML để tránh lỗi "totalPages cannot be found on null"
        model.addAttribute("pageData", pageData);
        model.addAttribute("listLoan", pageData.getContent());

        // Gửi lại giá trị ngày để các nút Next/Previous giữ được điều kiện lọc
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);

        if (pageData.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy lượt mượn nào trong khoảng thời gian này!");
        }

        return "Loan/loan_hien_thi";
    }
}
