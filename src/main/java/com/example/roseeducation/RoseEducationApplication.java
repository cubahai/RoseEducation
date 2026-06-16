package com.example.roseeducation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Chỉ để duy nhất dòng này, không có thêm chữ exclude nào bên cạnh
@SpringBootApplication
public class RoseEducationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoseEducationApplication.class, args);
    }
}