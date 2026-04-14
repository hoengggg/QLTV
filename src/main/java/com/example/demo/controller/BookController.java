package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.PublisherRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookController {
    @Autowired
    private BookRepository repoBook;

    @Autowired
    private PublisherRepository repoPublisher;

    @GetMapping("/book")
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

        Page<Book> pageData = repoBook.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);

        model.addAttribute("title", null);
        model.addAttribute("listBook", repoBook.findAll(PageRequest.of(pageIndex, 10)));
        return "Book/book_hien_thi";
    }

    @GetMapping("/book/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoBook.deleteById(id);
        return "redirect:/book";
    }

    @GetMapping("/book/add")
    public String addView(Model model){
        model.addAttribute("newBook", new Book());
        model.addAttribute("listPublisher", repoPublisher.findAll());
        return "Book/book_add";
    }

    @PostMapping("/book/add")
    public String add(Book book, Model model){
        model.addAttribute("listPublisher", repoPublisher.findAll());
        repoBook.save(book);
        return "redirect:/book";
    }

    @GetMapping("/book/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Book_Tim", repoBook.findById(id));
        model.addAttribute("listPublisher", repoPublisher.findAll());
        return "Book/book_detail";
    }

    @PostMapping("/book/update")
    public String update(@ModelAttribute("Book_Tim") Book book, Model model){
        model.addAttribute("listPublisher", repoPublisher.findAll());
        repoBook.save(book);
        return "redirect:/book";
    }

    @GetMapping("/book/search")
    public String search(@RequestParam("title") String title, @RequestParam(defaultValue = "0") int page, Model model){
        int pageIndex = (page < 1) ? 0 : page - 1;

        String searchKW = (title != null) ? title.trim() : "";

        Page<Book> pageData = repoBook.findByTitleContaining(searchKW, PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("listBook", pageData.getContent());
        model.addAttribute("title23", searchKW);
        return "Book/book_hien_thi";
    }

    @GetMapping("/book/loc")
    public String filterBook(
            @RequestParam("min") Float min,
            @RequestParam("max") Float max,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Chuyển đổi trang hiển thị (1, 2, 3...) thành chỉ số index (0, 1, 2...)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // Thực hiện truy vấn có phân trang
        Page<Book> pageData = repoBook.findByPopularityScoreBetween(min, max, PageRequest.of(pageIndex, 10));

        // Đưa dữ liệu sang HTML để fix lỗi "totalPages cannot be found on null"
        model.addAttribute("pageData", pageData);
        model.addAttribute("listBook", pageData.getContent());

        // Gửi lại giá trị min/max để giữ trạng thái lọc khi bấm chuyển trang
        model.addAttribute("minScore", min);
        model.addAttribute("maxScore", max);

        if (pageData.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy cuốn sách nào trong khoảng điểm này!");
        }

        return "Book/book_hien_thi";
    }
}
