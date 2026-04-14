package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleRepository repoRole;

    @GetMapping("/role")
    public String getAll(Model model, HttpSession session){
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser == null){
            return "redirect:/login";
        }

        String roleName = currentUser.getRole().getName();
        if(!"Admin".equalsIgnoreCase(roleName) && !"Librarian".equalsIgnoreCase(roleName) && !"Student".equalsIgnoreCase(roleName)){
            return "Login/403";
        }

        model.addAttribute("listRole", repoRole.findAll());
        return "Role/role_hien_thi";
    }

    @GetMapping("/role/add")
    public String addView(Model model){
        model.addAttribute("newRole", new Role());
        return "Role/role_add";
    }

    @PostMapping("/role/add")
    public String add(Role role){
        repoRole.save(role);
        return "redirect:/role";
    }

    @GetMapping("/role/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("Role_Tim", repoRole.findById(id).orElse(null));
        return "Role/role_detail";
    }

    @PostMapping("/role/update")
    public String update(@ModelAttribute("Role_Tim") Role role){
        repoRole.save(role);
        return "redirect:/role";
    }

    @GetMapping("/role/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        repoRole.deleteById(id);
        return "redirect:/role";
    }

    @GetMapping("/role/search-range")
    public String searchRange(
            @RequestParam("min") Integer min,
            @RequestParam("max") Integer max,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        // Chuyển đổi trang từ UI (1, 2...) sang index code (0, 1...)
        int pageIndex = (page < 1) ? 0 : page - 1;

        // Gọi repo với phân trang (10 bản ghi mỗi trang)
        Page<Role> pageData = repoRole.findByPermissionLevelBetween(min, max, PageRequest.of(pageIndex, 10));

        // Gửi dữ liệu sang HTML
        model.addAttribute("pageData", pageData);
        model.addAttribute("listRole", pageData.getContent());

        // Giữ lại min/max để các nút Previous/Next giữ được điều kiện lọc
        model.addAttribute("minSearch", min);
        model.addAttribute("maxSearch", max);

        if (pageData.isEmpty()) {
            model.addAttribute("message", "Cấp độ quyền hạn không tồn tại!");
        }

        return "Role/role_hien_thi";
    }
}
