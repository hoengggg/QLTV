package com.example.demo.controller;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuditLogController {
    @Autowired
    private AuditLogRepository repoAudit;

    @GetMapping("/auditlog")
    public String getAll(Model model, @RequestParam(defaultValue = "0") int page){
        // Đưa về Index của Spring (trang 1 -> index 0)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // 1. Lấy đối tượng Page
        Page<AuditLog> pageData = repoAudit.findAll(PageRequest.of(pageIndex, 10));

        // 2. Đẩy cả 2 biến ra giống như hàm search
        model.addAttribute("pageData", pageData);

        model.addAttribute("description", null); // Quan trọng: set null để phân biệt với search
        model.addAttribute("listAuditLog", repoAudit.findAll(PageRequest.of(pageIndex, 10)));
        return "AuditLog/audit_log_hien_thi";
    }

    @GetMapping("/auditlog/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoAudit.deleteById(id);
        return "redirect:/auditlog";
    }

    @GetMapping("/auditlog/add")
    public String addView(Model model){
        model.addAttribute("newAuditLog", new AuditLog());
        model.addAttribute("listAction", repoAudit.findDistinctActionBy());
        model.addAttribute("listTargerType", repoAudit.findDistinctTargetTypeBy());
        return "AuditLog/auditlog_add";
    }

    @PostMapping("auditlog/add")
    public String add(AuditLog auditLog, Model model){
        repoAudit.save(auditLog);
        model.addAttribute("listAction", repoAudit.findDistinctActionBy());
        model.addAttribute("listTargerType", repoAudit.findDistinctTargetTypeBy());
        return "redirect:/auditlog";
    }

    @GetMapping("/auditlog/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("AuditLog_Tim", repoAudit.findById(id));
        model.addAttribute("listAction", repoAudit.findDistinctActionBy());
        model.addAttribute("listTargerType", repoAudit.findDistinctTargetTypeBy());
        return "AuditLog/auditlog_detail";
    }

    @PostMapping("/auditlog/update")
    public String update(@ModelAttribute("AuditLog_Tim") AuditLog auditLog, Model model){
        repoAudit.save(auditLog);
        model.addAttribute("listAction", repoAudit.findDistinctActionBy());
        model.addAttribute("listTargerType", repoAudit.findDistinctTargetTypeBy());
        return "redirect:/auditlog";
    }

    @GetMapping("/auditlog/search")
    public String search(@RequestParam(value = "description", required = false) String description,
                         @RequestParam(defaultValue = "0") int page,
                         Model model){

        // Đưa về Index của Spring (trang 1 -> index 0)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // 1. Xử lý từ khóa: Nếu null hoặc chỉ toàn dấu cách thì đưa về chuỗi rỗng
        String searchKeyword = (description != null) ? description.trim() : "";

        // 2. Gọi Repository với từ khóa đã xử lý
        Page<AuditLog> pageData = repoAudit.findByDescriptionContaining(searchKeyword, PageRequest.of(pageIndex, 10));

        // 3. Đẩy dữ liệu ra View
        model.addAttribute("pageData", pageData);
        model.addAttribute("listAuditLog", pageData.getContent());
        model.addAttribute("description23", searchKeyword); // Dùng cái này để "nuôi" link phân trang

        return "AuditLog/audit_log_hien_thi";
    }
}
