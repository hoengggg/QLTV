package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.LoginRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    private LoginRepository repo;

    @GetMapping("/login")
    public String loginForm(){
        return "Login/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model){
        User user = repo.findByUsernameAndPassword(username, password);

        if(user != null){
            if(!"Active".equalsIgnoreCase(user.getStatus())){
                model.addAttribute("message2", "Tài khoản này đang bị khóa");
                return "Login/login";
            }

            session.setAttribute("currentUser", user);
            return "redirect:/loan";
        }else{
            model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu");
            return "Login/login";
        }
    }
}
