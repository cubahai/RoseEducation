package com.example.roseeducation.controller;

import com.example.roseeducation.entity.Role;
import com.example.roseeducation.entity.User;
import com.example.roseeducation.repository.RoleRepository;
import com.example.roseeducation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    // Tiêm các dependency (Repository và Encoder) vào Controller
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/dang-nhap")
    public String showLoginPage() {
        return "dang-nhap";
    }



    @GetMapping("/dang-ky")
    public String showDangKyPage() {
        return "dang-ky";
    }

    // XỬ LÝ LƯU DATABASE TẠI ĐÂY
    @PostMapping("/dang-ky")
    public String handleDangKy(@RequestParam("fullName") String fullName,
                               @RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword) {

        // 1. Kiểm tra mật khẩu nhập lại có khớp không
        if (!password.equals(confirmPassword)) {
            return "redirect:/dang-ky?error=password_mismatch";
        }

        // 2. Kiểm tra xem tên đăng nhập đã bị người khác dùng chưa
        if (userRepository.findByUsername(username) != null) {
            return "redirect:/dang-ky?error=username_exists";
        }

        // 3. Tạo đối tượng User mới và gán dữ liệu từ Form
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        // BẮT BUỘC MÃ HÓA MẬT KHẨU TRƯỚC KHI LƯU
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEnabled(true);

        // 4. Tìm quyền mặc định (ROLE_USER) dưới DB và cấp cho tài khoản này
        Role defaultRole = roleRepository.findByName("ROLE_USER");
        if (defaultRole != null) {
            newUser.getRoles().add(defaultRole);
        }

        // 5. Chính thức ra lệnh lưu xuống cơ sở dữ liệu!
        userRepository.save(newUser);

        // Sau khi lưu thành công, quay về trang đăng nhập
        return "redirect:/dang-nhap?success=registered";
    }
}