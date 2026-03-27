package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private String getAll(Model model){
        model.addAttribute("listCategory", repoCategory.findAll());
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
    public String search(@RequestParam("name") String name, Model model){
        model.addAttribute("listCategory", repoCategory.findByNameContaining(name));
        return "Category/category_hien_thi";
    }

    @GetMapping("/category/search-range")
    public String searchRange(@RequestParam("min") Integer min, @RequestParam("max") Integer max, Model model) {
        List<Category> list = repoCategory.findByTotalBooksBetween(min, max);
        model.addAttribute("listCategory", list);
        if (list.isEmpty()) model.addAttribute("message", "Không có thể loại nào có số lượng sách này!");
        return "Category/category_hien_thi";
    }
}
