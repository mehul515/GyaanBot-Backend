package com.gyaanbot.course_service.repository;

import com.gyaanbot.course_service.entity.Course;
import com.gyaanbot.course_service.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByCourse(Course course);
    void deleteByCourse(Course course);
}
