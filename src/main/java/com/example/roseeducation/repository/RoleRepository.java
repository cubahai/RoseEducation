package com.example.roseeducation.repository;

import com.example.roseeducation.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Cú pháp chuẩn của Java để tìm quyền hạn theo tên
    Role findByName(String name);
}