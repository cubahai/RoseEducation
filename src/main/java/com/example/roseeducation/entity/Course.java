package com.example.roseeducation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenKhoaHoc;
    private Long hocPhi;

    @Column(columnDefinition = "TEXT")
    private String ghiChu;

    private String hinhAnhUrl;

    // Trường lưu thời gian tạo giúp sắp xếp khóa học nào thêm sau sẽ nằm dưới cùng
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public void setTenKhoaHoc(String tenKhoaHoc) { this.tenKhoaHoc = tenKhoaHoc; }

    public Long getHocPhi() { return hocPhi; }
    public void setHocPhi(Long hocPhi) { this.hocPhi = hocPhi; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu ) { this.ghiChu = ghiChu; }

    public String getHinhAnhUrl() { return hinhAnhUrl; }
    public void setHinhAnhUrl(String hinhAnhUrl) { this.hinhAnhUrl = hinhAnhUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}