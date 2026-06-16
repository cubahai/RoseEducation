package com.example.roseeducation.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // mappedBy trỏ tới tên biến 'roles' trong class User.
    // Điều này thiết lập mối quan hệ 2 chiều (Bidirectional)
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // --- Bắt buộc phải có Getters và Setters (Nếu bạn dùng thư viện Lombok thì có thể xóa đoạn dưới này và thêm @Data ở đầu class) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}