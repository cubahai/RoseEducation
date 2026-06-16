package com.example.roseeducation.dto

import org.springframework.web.multipart.MultipartFile

class CourseViewModel {
    // --- Bắt buộc phải có các hàm Getters và Setters để Thymeleaf đọc dữ liệu ---
    var tenKhoaHoc: String? = null
    var hocPhi: String? = null
    var ghiChu: String? = null
    var hinhAnh: MultipartFile? = null // Hứng file nhị phân của ảnh truyền lên từ form
    var hinhAnhUrl: String? = null // Lưu đường dẫn file ảnh để hiển thị lên thẻ <img>
}