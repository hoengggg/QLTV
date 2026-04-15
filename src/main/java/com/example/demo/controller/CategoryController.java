package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepository repoCategory;

    @Autowired
    private BookRepository repoBook;

    @GetMapping("/category")
    private String getAll(Model model, @RequestParam(defaultValue = "0") int page, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName) && !"Librarian".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

        int pageIndex = (page < 1) ? 0 : page - 1;

        Page<Category> pageData = repoCategory.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("name", null);
        model.addAttribute("listCategory", repoCategory.findAll(PageRequest.of(pageIndex, 10)));
        model.addAttribute("name23", null);
        return "Category/category_hien_thi";
    }

    @GetMapping("/category/delete/{id}")
    private String delete(@PathVariable("id") Long id){
        repoCategory.deleteById(id);
        return "redirect:/category";
    }

    @GetMapping("/category/add")
    public String addView(Model model){
        model.addAttribute("newCategory", new Category());
        model.addAttribute("listBook", repoBook.findAll());
        return "Category/category_add";
    }

    @PostMapping("/category/add")
    public String add(Model model, Category category){
        model.addAttribute("listBook", repoBook.findAll());
        repoCategory.save(category);
        return "redirect:/category";
    }

    @GetMapping("/category/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Category_Tim", repoCategory.findById(id));
        model.addAttribute("listBook", repoBook.findAll());
        return "Category/category_detail";
    }

    @PostMapping("/category/update")
    public String update(@ModelAttribute("Category_Tim") Category category, Model model){
        model.addAttribute("listBook", repoBook.findAll());
        repoCategory.save(category);
        return "redirect:/category";
    }

    @GetMapping("/category/search")
    public String search(@RequestParam("name") String name, @RequestParam(defaultValue = "0") int page, Model model){
        int pageIndex = (page < 1) ? 0 : page - 1;

        String searchKW = (name != null) ? name.trim() : "";

        Page<Category> pageData = repoCategory.findByNameContaining(searchKW, PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("listCategory", pageData.getContent());
        model.addAttribute("name23", searchKW);
        return "Category/category_hien_thi";
    }

    @GetMapping("/category/search-range")
    public String searchRange(
            @RequestParam("min") Integer min,
            @RequestParam("max") Integer max,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Tính toán số trang (Trang 1 ở UI tương ứng với index 0 trong code)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // Gọi repo với phân trang (ví dụ hiển thị 10 thể loại trên 1 trang)
        Page<Category> pageData = repoCategory.findByTotalBooksBetween(min, max, PageRequest.of(pageIndex, 10));

        // Đưa dữ liệu sang HTML để xử lý hiển thị và phân trang
        model.addAttribute("pageData", pageData);
        model.addAttribute("listCategory", pageData.getContent());

        // Gửi lại min, max để các nút chuyển trang giữ được điều kiện lọc
        model.addAttribute("minSearch", min);
        model.addAttribute("maxSearch", max);

        if (pageData.isEmpty()) {
            model.addAttribute("message", "Không có thể loại nào có số lượng sách này!");
        }

        return "Category/category_hien_thi";
    }
}
