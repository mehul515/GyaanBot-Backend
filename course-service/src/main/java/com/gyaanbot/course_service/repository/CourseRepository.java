package com.gyaanbot.course_service.repository;

import com.gyaanbot.course_service.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByIsPublicTrue();
}
