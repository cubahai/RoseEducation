package com.example.roseeducation.controller;

import com.example.roseeducation.dto.CourseViewModel;
import com.example.roseeducation.entity.Course;
import com.example.roseeducation.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("java.io.tmpdir"), "roseeducation", "uploads");

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }

    @GetMapping("/admin/xoa-khoa-hoc/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
        }
        return "redirect:/index";
    }

    @PostMapping("/admin/them-khoa-hoc")
    public String addCourse(CourseViewModel courseViewModel) throws IOException {
        MultipartFile file = courseViewModel.getHinhAnh();
        String savedImgUrl = null;

        if (file != null && !file.isEmpty()) {
            File uploadFolder = UPLOAD_DIR.toFile();
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadFolder.toPath().resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            savedImgUrl = "/uploads/" + uniqueFileName;
        }

        Course newCourse = new Course();
        newCourse.setTenKhoaHoc(courseViewModel.getTenKhoaHoc());
        if (courseViewModel.getHocPhi() != null && !courseViewModel.getHocPhi().isEmpty()) {
            newCourse.setHocPhi(Long.parseLong(courseViewModel.getHocPhi()));
        } else {
            newCourse.setHocPhi(0L);
        }
        newCourse.setGhiChu(courseViewModel.getGhiChu());
        newCourse.setHinhAnhUrl(savedImgUrl);

        courseRepository.save(newCourse);

        return "redirect:/index";
    }
}
