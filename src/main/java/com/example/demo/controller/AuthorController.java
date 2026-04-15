package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.model.User;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthorController {
    @Autowired
    private AuthorRepository repoAuthor;

    @Autowired
    private BookRepository repoBook;

    @GetMapping("/author")
    public String getAll(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

        int pageIndex = (page < 1) ? 0 : page - 1;

        Page<Author> pageData = repoAuthor.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);

        model.addAttribute("name23", null);
        model.addAttribute("listAuthor", repoAuthor.findAll(PageRequest.of(pageIndex, 10)));
        model.addAttribute("name23", null);
        return "Author/author_hien_thi";
    }

    @GetMapping("/author/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoAuthor.deleteById(id);
        return "redirect:/author";
    }

    @GetMapping("/author/add")
    public String addView(Model model){
        model.addAttribute("newAuthor", new Author());
        return "Author/author_add";
    }

    @PostMapping("/author/add")
    public String add(Author author){
        repoAuthor.save(author);
        return "redirect:/author";
    }

    @GetMapping("/author/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Author_Tim", repoAuthor.findById(id));
        return "Author/author_detail";
    }

    @PostMapping("/author/update")
    public String update(@ModelAttribute("Author_Tim") Author author){
        repoAuthor.save(author);
        return "redirect:/author";
    }

    @GetMapping("/author/search")
    public String search(@RequestParam("name") String name, @RequestParam(defaultValue = "0") int page, Model model){
        int pageIndex = (page < 1) ? 0 : page - 1;

        String searchKW = (name != null) ? name.trim() : "";

        Page<Author> pageData = repoAuthor.findByNameContaining(searchKW, PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("listAuthor", pageData.getContent());
        model.addAttribute("name23", searchKW);
        return "Author/author_hien_thi";
    }

    @GetMapping("/author/search-range")
    public String searchRange(
            @RequestParam("min") Integer min,
            @RequestParam("max") Integer max,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Xử lý index trang (Trang 1 ở UI = trang 0 ở DB)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // Gọi repo với phân trang (ví dụ 10 bản ghi/trang)
        Page<Author> pageData = repoAuthor.findByAwardsCountBetween(min, max, PageRequest.of(pageIndex, 10));

        // Đưa dữ liệu sang HTML - Đây là bước quan trọng để không bị lỗi null
        model.addAttribute("pageData", pageData);
        model.addAttribute("listAuthor", pageData.getContent());

        // Gửi lại min/max để giữ trạng thái lọc khi bấm Next/Previous
        model.addAttribute("minSearch", min);
        model.addAttribute("maxSearch", max);

        if (pageData.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy tác giả nào!");
        }

        return "Author/author_hien_thi";
    }
}
