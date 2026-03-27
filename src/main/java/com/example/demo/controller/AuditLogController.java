package com.example.demo.controller;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuditLogController {
    @Autowired
    private AuditLogRepository repoAudit;

    @GetMapping("/auditlog")
    public String getAll(Model model){
        model.addAttribute("listAuditLog", repoAudit.findAll());
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
    public String search(@RequestParam("description") String description, Model model){
        model.addAttribute("listAuditLog", repoAudit.findByDescriptionContaining(description));
        return "AuditLog/audit_log_hien_thi";
    }
}
