package com.gyaanbot.course_service.service;

import com.gyaanbot.course_service.dto.CourseRequest;
import com.gyaanbot.course_service.entity.Course;
import com.gyaanbot.course_service.repository.CourseRepository;

import com.gyaanbot.course_service.repository.DocumentRepository;
import com.gyaanbot.course_service.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(CourseRequest request, Long teacherId) {
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacherId(teacherId);
        course.setCreatedAt(LocalDateTime.now());
        if(request.getIsPublic()==null){
            course.setIsPublic(false);
        }else{
            course.setIsPublic(true);
        }

        return courseRepository.save(course);
    }

    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<Course> getAllCourses() {
        return courseRepository.findByIsPublicTrue();
    }

    @Transactional
    public void deleteCourse(Long courseId, Long teacherId) {
        Course course = getCourseById(courseId);

        if (!course.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Unauthorized: You are not the owner of this course.");
        }
        enrollmentRepository.deleteByCourse(course);

        // Step 2: delete documents
        documentRepository.deleteByCourse(course);

        // Step 3: delete course
        courseRepository.delete(course);
    }

    public void togglePublicStatus(Long courseId, Long teacherId) {
        Course course = getCourseById(courseId);

        if (!course.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Unauthorized: You are not the owner of this course.");
        }

        Boolean currentStatus = course.getIsPublic() != null && course.getIsPublic();
        course.setIsPublic(!currentStatus);

        courseRepository.save(course);
    }

    public Course getCourseById(Long courseId, Long teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getTeacherId().equals(teacherId) && !course.getIsPublic()) {
            throw new RuntimeException("Access denied: You are not the owner of this course.");
        }

        return course;
    }


}
