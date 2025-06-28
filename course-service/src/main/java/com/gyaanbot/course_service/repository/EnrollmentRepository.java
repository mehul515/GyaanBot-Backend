package com.gyaanbot.course_service.repository;

import com.gyaanbot.course_service.entity.Course;
import com.gyaanbot.course_service.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourseId(Long courseId);
    List<Enrollment> findByStudentId(Long studentId);
    Optional<Enrollment> findByCourseAndStudentId(Course course, Long studentId);
    boolean existsByCourseAndStudentId(Course course, Long studentId);
    void deleteByCourse(Course course);
}
