package com.example.roseeducation.controller;

import com.example.roseeducation.dto.CourseViewModel;
import com.example.roseeducation.entity.Course;
import com.example.roseeducation.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class AdminController {

    @Autowired
    private CourseRepository courseRepository; // Tiêm repository vào để lưu dữ liệu vĩnh viễn

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }
    @GetMapping("/admin/xoa-khoa-hoc/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        // Kiểm tra xem khóa học có tồn tại trong Database không trước khi xóa
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        }
        // Xóa xong chuyển hướng quay trở lại trang chủ để cập nhật giao diện
        return "redirect:/index";
    }
    @PostMapping("/admin/them-khoa-hoc")
    public String addCourse(CourseViewModel courseViewModel) throws IOException {
        MultipartFile file = courseViewModel.getHinhAnh();
        String savedImgUrl = null;

        if (file != null && !file.isEmpty()) {
            File uploadFolder = new File("src/main/resources/static/uploads");
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadFolder.getAbsolutePath(), uniqueFileName);
            Files.write(filePath, file.getBytes());

            savedImgUrl = "/uploads/" + uniqueFileName;
        }

        // CHUYỂN ĐỔI TỪ DTO SANG ENTITY ĐỂ LƯU XUỐNG MYSQL VĨNH VIỄN
        Course newCourse = new Course();
        newCourse.setTenKhoaHoc(courseViewModel.getTenKhoaHoc());
        if (courseViewModel.getHocPhi() != null && !courseViewModel.getHocPhi().isEmpty()) {
            newCourse.setHocPhi(Long.parseLong(courseViewModel.getHocPhi()));
        } else {
            newCourse.setHocPhi(0L); // Nếu không nhập học phí thì mặc định gán bằng 0
        }
        newCourse.setGhiChu(courseViewModel.getGhiChu());
        newCourse.setHinhAnhUrl(savedImgUrl);

        // Ra lệnh cho JPA lưu xuống database, khóa học mới tự động nằm ở cuối bảng dữ liệu
        courseRepository.save(newCourse);

        return "redirect:/index";
    }
}