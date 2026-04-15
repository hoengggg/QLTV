package com.example.demo.controller;

import com.example.demo.model.Publisher;
import com.example.demo.model.User;
import com.example.demo.repository.PublisherRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PublisherController {
    @Autowired
    private PublisherRepository repoPublisher;

    @GetMapping("/publisher")
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

        Page<Publisher> pageData = repoPublisher.findAll(PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("name", null);
        model.addAttribute("listPublisher", repoPublisher.findAll(PageRequest.of(pageIndex, 10)));
        model.addAttribute("name23", null);
        return "Publisher/publisher_hien_thi";
    }

    @GetMapping("/publisher/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoPublisher.deleteById(id);
        return "redirect:/publisher";
    }

    @GetMapping("/publisher/add")
    public String addView(Model model){
        model.addAttribute("newPublisher", new Publisher());
        return "Publisher/publisher_add";
    }

    @PostMapping("/publisher/add")
    public String add(Publisher publisher){
        repoPublisher.save(publisher);
        return "redirect:/publisher";
    }

    @GetMapping("/publisher/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Publisher_Tim", repoPublisher.findById(id));
        return "Publisher/publisher_detail";
    }

    @PostMapping("/publisher/update")
    public String update(@ModelAttribute("Publisher_Tim") Publisher publisher){
        repoPublisher.save(publisher);
        return "redirect:/publisher";
    }

    @GetMapping("/publisher/search")
    public String search(@RequestParam("name") String name, @RequestParam(defaultValue = "0") int page, Model model){
        int pageIndex = (page < 1) ? 0 : page - 1;

        String searchKW = (name != null) ? name.trim() : "";

        Page<Publisher> pageData = repoPublisher.findByNameContaining(searchKW, PageRequest.of(pageIndex, 10));

        model.addAttribute("pageData", pageData);
        model.addAttribute("listPublisher", pageData.getContent());
        model.addAttribute("name23", searchKW);
        return "Publisher/publisher_hien_thi";
    }

    @GetMapping("/publisher/loc")
    public String filterPublisher(
            @RequestParam("min") Double min,
            @RequestParam("max") Double max,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Tính toán index trang (Trang 1 ở UI = index 0 ở DB)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // Gọi repo với phân trang (mỗi trang hiển thị 10 nhà xuất bản)
        Page<Publisher> pageData = repoPublisher.findByAverageLoanDaysBetween(min, max, PageRequest.of(pageIndex, 10));

        // Đưa dữ liệu sang HTML để fix lỗi "totalPages cannot be found on null"
        model.addAttribute("pageData", pageData);
        model.addAttribute("listPublisher", pageData.getContent());

        // Gửi lại min/max để các nút Previous/Next giữ được điều kiện lọc
        model.addAttribute("minSearch", min);
        model.addAttribute("maxSearch", max);

        if (pageData.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy nhà xuất bản nào trong khoảng này!");
        }

        return "Publisher/publisher_hien_thi";
    }
}
