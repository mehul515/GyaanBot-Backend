package com.gyaanbot.course_service.service;

import com.gyaanbot.course_service.client.UserClient;
import com.gyaanbot.course_service.dto.UserResponse;
import com.gyaanbot.course_service.entity.Course;
import com.gyaanbot.course_service.entity.Enrollment;
import com.gyaanbot.course_service.repository.EnrollmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserClient userClient;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public void enrollStudentByTeacher(Course course, Long studentId) {

        boolean exists = enrollmentRepository.existsByCourseAndStudentId(course, studentId);
        if (exists) {
            throw new RuntimeException("Student already enrolled");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudentId(studentId);
        enrollmentRepository.save(enrollment);
    }

    public void enrollStudentByStudent(Course course, Long studentId) {

        if(!course.getIsPublic()){
            throw new RuntimeException("Cannot Enroll to the Course!");
        }

        boolean exists = enrollmentRepository.existsByCourseAndStudentId(course, studentId);
        if (exists) {
            throw new RuntimeException("Student already enrolled");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudentId(studentId);
        enrollmentRepository.save(enrollment);
    }

    public void removeEnrollment(Course course, Long studentId, Long teacherId) {
        if (!course.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Unauthorized: You are not the owner of this course.");
        }
        Enrollment enrollment = enrollmentRepository
                .findByCourseAndStudentId(course, studentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollmentRepository.delete(enrollment);
    }

    public List<UserResponse> getEnrollmentsByCourse(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        return enrollments.stream().map(enrollment -> {
            UserResponse student = userClient.getUserById(enrollment.getStudentId());
            return new UserResponse(student.getRole(), student.getEmail(),student.getId());
        }).collect(Collectors.toList());
    }


    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }
}
