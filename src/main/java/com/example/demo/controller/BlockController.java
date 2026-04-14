package com.example.demo.controller;

import com.example.demo.dto.BlockUserDto;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173") //cho phép vue js kết nối đến đây
public class BlockController {
    @Autowired
    private UserRepository repo;

    @GetMapping("/block")
    public List<BlockUserDto> getAll(Model model){
        return repo.getAllBlockUser();
    }
}
