package com.example.roseeducation.repository;

import com.example.roseeducation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // <--- Chữ interface là bắt buộc
    User findByUsername(String username);
}