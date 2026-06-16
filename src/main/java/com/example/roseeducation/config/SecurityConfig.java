package com.example.roseeducation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 🌟 BẠN BỔ SUNG THÊM "/NguyenThiDieuHuong.jpg" VÀO HÀNG NÀY NHÉ:
                        .requestMatchers("/", "/index", "/dang-nhap", "/dang-ky", "/style.css", "/uploads/**", "/RoseHọcThử.jpg", "/NguyenThiDieuHuong.jpg").permitAll()

                        // Yêu cầu quyền ADMIN mới được vào đường dẫn /admin/**
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                // Cấu hình form login và tự động điều hướng giữ nguyên vẹn bên dưới...
                .formLogin(form -> form
                        .loginPage("/dang-nhap")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) {
                                response.sendRedirect("/admin");
                            } else {
                                response.sendRedirect("/index");
                            }
                        })
                        .permitAll()
                )
                // 🌟 THÊM ĐOẠN CẤU HÌNH ĐĂNG XUẤT VÀO ĐÂY
                .logout(logout -> logout
                        .logoutUrl("/dang-xuat")              // Đường dẫn kích hoạt hành động đăng xuất
                        .logoutSuccessUrl("/index?logout")     // Đường dẫn sau khi đăng xuất thành công
                        .invalidateHttpSession(true)           // Hủy session làm việc hiện tại
                        .clearAuthentication(true)             // Xóa thông tin xác thực
                        .permitAll()
                );

        return http.build();
    }
}