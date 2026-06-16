package com.example.roseeducation.service;

import com.example.roseeducation.entity.Role;
import com.example.roseeducation.entity.User;
import com.example.roseeducation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm user trong Database thông qua UserRepository
        User user = userRepository.findByUsername(username);

        // Nếu không tìm thấy tài khoản, ném ra lỗi
        if (user == null) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại!");
        }

        // 2. Lấy danh sách các quyền (Role) của user từ DB và chuyển thành định dạng GrantedAuthority của Spring Security
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        // 3. Trả về đối tượng UserDetails chứa thông tin để Spring Security tự động kiểm tra mật khẩu
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );
    }
}