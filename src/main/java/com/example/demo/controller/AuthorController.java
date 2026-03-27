package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String getAll(Model model){
        model.addAttribute("listAuthor", repoAuthor.findAll());
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
    public String search(@RequestParam("name") String name, Model model){
        model.addAttribute("listAuthor", repoAuthor.findByNameContaining(name));
        return "Author/author_hien_thi";
    }

    @GetMapping("/author/search-range")
    public String searchRange(@RequestParam("min") Integer min, @RequestParam("max") Integer max, Model model) {
        List<Author> list = repoAuthor.findByAwardsCountBetween(min, max);
        model.addAttribute("listAuthor", list);
        if (list.isEmpty()) model.addAttribute("message", "Không tìm thấy tác giả nào đạt số giải thưởng này!");
        return "Author/author_hien_thi";
    }
}
