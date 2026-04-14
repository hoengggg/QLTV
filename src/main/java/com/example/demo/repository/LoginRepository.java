package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

public interface LoginRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
    //tên hàm repo này phải viết theo gợi ý của intelij, cụ thể hơn là cái nội dung tìm là username và password phải viết giống
    //entity, như này findByNameAndPass là sai
}
