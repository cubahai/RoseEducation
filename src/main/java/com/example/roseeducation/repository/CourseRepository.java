package com.example.roseeducation.repository;

import com.example.roseeducation.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Sắp xếp theo thời gian tăng dần: Khóa học cũ ở trên, khóa học mới thêm sẽ nằm ở dưới cùng
    List<Course> findAllByOrderByCreatedAtAsc();
}