package com.example.roseeducation.controller;

import com.example.roseeducation.entity.Course;
import com.example.roseeducation.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/index")
    public String showIndexPage(Model model, Authentication authentication) {
        // 1. Lấy danh sách khóa học từ Database
        List<Course> DBList = courseRepository.findAllByOrderByCreatedAtAsc();
        model.addAttribute("coursesFromDB", DBList);

        if (!DBList.isEmpty()) {
            Course latestCourse = DBList.get(DBList.size() - 1);
            model.addAttribute("course", latestCourse);
        }

        // 2. Kiểm tra trạng thái đăng nhập và phân quyền ADMIN
        boolean isAdmin = false;
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());

            // Duyệt qua danh sách quyền để kiểm tra xem có quyền ROLE_ADMIN hay không
            isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }

        // Truyền biến trạng thái isAdmin qua file HTML (true là Admin, false là User bình thường)
        model.addAttribute("isAdmin", isAdmin);

        return "Index";
    }
}