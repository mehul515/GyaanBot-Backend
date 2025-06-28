package com.gyaanbot.user_service.controller;

import com.gyaanbot.user_service.dto.StudentProfileRequest;
import com.gyaanbot.user_service.dto.TeacherProfileRequest;
import com.gyaanbot.user_service.dto.UserResponse;
import com.gyaanbot.user_service.entity.User;
import com.gyaanbot.user_service.service.StudentService;
import com.gyaanbot.user_service.service.TeacherService;
import com.gyaanbot.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        String email = (String) auth.getPrincipal();
        return ResponseEntity.ok(userService.getCurrentUser(email));
    }

    @GetMapping("/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        System.out.println("Email : " + email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/student/profile")
    public ResponseEntity<?> getStudentProfile(Authentication auth) {
        String email = (String) auth.getPrincipal();
        return ResponseEntity.ok(studentService.getProfile(email));
    }

    @PutMapping("/student/profile")
    public ResponseEntity<?> updateStudentProfile(Authentication auth, @RequestBody StudentProfileRequest req) {
        String email = (String) auth.getPrincipal();
        studentService.updateProfile(email, req);
        return ResponseEntity.ok("Student profile updated");
    }

    @GetMapping("/student/profile/{studentId}")
    public ResponseEntity<?> getStudentProfileById(@PathVariable Long studentId) {
        UserResponse student = userService.getUserById(studentId);
        return ResponseEntity.ok(studentService.getProfile(student.getEmail()));
    }

    @GetMapping("/teacher/profile")
    public ResponseEntity<?> getTeacherProfile(Authentication auth) {
        String email = (String) auth.getPrincipal();
        return ResponseEntity.ok(teacherService.getProfile(email));
    }

    @GetMapping("/teacher/profile/{teacherId}")
    public ResponseEntity<?> getTeacherProfileById(@PathVariable Long teacherId) {
        UserResponse teacher = userService.getUserById(teacherId);
        return ResponseEntity.ok(teacherService.getProfile(teacher.getEmail()));
    }

    @PutMapping("/teacher/profile")
    public ResponseEntity<?> updateTeacherProfile(Authentication auth, @RequestBody TeacherProfileRequest req) {
        String email = (String) auth.getPrincipal();
        teacherService.updateProfile(email, req);
        return ResponseEntity.ok("Teacher profile updated");
    }

}
