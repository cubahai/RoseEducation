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
                        .requestMatchers("/", "/index", "/dang-nhap", "/dang-ky", "/style.css", "/uploads/**", "/RoseHọcThử.jpg", "/NguyenThiDieuHuong.jpg").permitAll()
                        .requestMatchers("/admin", "/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/dang-nhap")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) {
                                response.sendRedirect("/admin");
                            } else {
                                response.sendRedirect("/index");
                            }
                        })
                        .failureUrl("/dang-nhap?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/dang-xuat")
                        .logoutSuccessUrl("/index?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }
}