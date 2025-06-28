package com.gyaanbot.course_service.controller;

import com.gyaanbot.course_service.client.UserClient;
import com.gyaanbot.course_service.config.JwtUtil;
import com.gyaanbot.course_service.dto.CourseRequest;
import com.gyaanbot.course_service.dto.DocumentRequest;
import com.gyaanbot.course_service.dto.UserResponse;
import com.gyaanbot.course_service.entity.Course;
import com.gyaanbot.course_service.entity.Document;
import com.gyaanbot.course_service.entity.Enrollment;
import com.gyaanbot.course_service.service.CourseService;
import com.gyaanbot.course_service.service.DocumentService;
import com.gyaanbot.course_service.service.EnrollmentService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtUtil jwtUtil;

    private String extractToken(HttpServletRequest req) {
        return req.getHeader("Authorization").replace("Bearer ", "");
    }

    private UserResponse getCurrentUser(HttpServletRequest req) {
        String jwt = extractToken(req);
        return userClient.getCurrentUser("Bearer " + jwt);
    }

    // 🔍 Get Course by ID (No role check)
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId, HttpServletRequest req) {
        UserResponse teacher = getCurrentUser(req);
        Course course = courseService.getCourseById(courseId, teacher.getId());

        return ResponseEntity.ok(course);
    }


    // 👨‍🏫 TEACHER — Create Course
    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody CourseRequest r, HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);
        if (!"TEACHER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can create courses.");
        }
        return ResponseEntity.ok(courseService.createCourse(r, user.getId()));
    }

    // 👨‍🏫 TEACHER — My Courses
    @GetMapping("/my-courses")
    public ResponseEntity<?> myCourses(HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);
        if (!"TEACHER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can view their courses.");
        }
        return ResponseEntity.ok(courseService.getCoursesByTeacher(user.getId()));
    }

    // 👨‍🏫 TEACHER — View Enrolled Students
    @GetMapping("/{courseId}/students")
    public ResponseEntity<?> getStudents(@PathVariable Long courseId, HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);
        if (!"TEACHER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can view enrolled students.");
        }
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    // 👨‍🏫 TEACHER — Enroll Student
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<String> enrollStudent(
            @PathVariable Long courseId,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String token,
            HttpServletRequest req
    ) {
        UserResponse teacher = getCurrentUser(req);
        if (!"TEACHER".equalsIgnoreCase(teacher.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can enroll students.");
        }

        String studentEmail = body.get("email");
        UserResponse student = userClient.getUserByEmail(studentEmail, token);

        if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
            return ResponseEntity.badRequest().body("Only students can be enrolled.");
        }

        System.out.println("Course ID : " + courseId);

        Course course = courseService.getCourseById(courseId);
        enrollmentService.enrollStudentByTeacher(course, student.getId());

        return ResponseEntity.ok("Student enrolled.");
    }

    @PutMapping("/{courseId}/toggle-public")
    public ResponseEntity<?> togglePublicStatus(@PathVariable Long courseId, HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);

        if (!"TEACHER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can update course visibility.");
        }

        courseService.togglePublicStatus(courseId, user.getId());
        return ResponseEntity.ok("Course visibility toggled.");
    }


    // 👨‍🏫 TEACHER — Remove Enrolled Student
    @DeleteMapping("/{courseId}/remove-student")
    public ResponseEntity<String> removeStudentFromCourse(
            @PathVariable Long courseId,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String token,
            HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);
        if (!"TEACHER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can remove students.");
        }

        String studentEmail = body.get("email");
        UserResponse student = userClient.getUserByEmail(studentEmail, token);

        Course course = courseService.getCourseById(courseId);
        enrollmentService.removeEnrollment(course, student.getId(), user.getId());
        return ResponseEntity.ok("Student removed from course.");
    }

    // 🎓 STUDENT — All Courses
    @GetMapping
    public List<Course> allCourses() {
        return courseService.getAllCourses();
    }

    // 🎓 STUDENT — Join Course
    @PostMapping("/{courseId}/join")
    public ResponseEntity<String> joinCourse(
            @PathVariable Long courseId,
            HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);
        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only students can join courses.");
        }

        Course c = courseService.getCourseById(courseId);
        enrollmentService.enrollStudentByStudent(c, user.getId());
        return ResponseEntity.ok("Enrolled successfully.");
    }

    // 🎓 STUDENT — My Enrollments
    @GetMapping("/enrolled")
    public ResponseEntity<?> myEnrollments(HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);
        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only students can view enrollments.");
        }

        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(user.getId()));
    }

    // 📄 — List course documents (no role check)
    @GetMapping("/{courseId}/documents")
    public List<Document> listDocuments(@PathVariable Long courseId) {
        Course c = courseService.getCourseById(courseId);
        return documentService.listByCourse(c);
    }

    // 📄 — Upload document (only TEACHER)
    @PostMapping("/{courseId}/documents/upload")
    public ResponseEntity<?> uploadDocument(
            @PathVariable Long courseId,
            @ModelAttribute DocumentRequest request,
            HttpServletRequest req) throws IOException {

        UserResponse user = getCurrentUser(req);
        if (!"TEACHER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can upload documents.");
        }

        Course course = courseService.getCourseById(courseId);
        Document doc = documentService.uploadAndSave(
                request.getFile(), request.getName(), request.getDescription(), course
        );

        return ResponseEntity.ok(doc);
    }

    // 🗑️ Delete a course (only TEACHER)
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId, HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);
        System.out.println("Received!!!!!");
        if (!"TEACHER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can delete courses.");
        }

        courseService.deleteCourse(courseId, user.getId());
        return ResponseEntity.ok("Course deleted.");
    }

    // 🗑️ Delete a document (only TEACHER)
    @DeleteMapping("/{courseId}/documents/{docId}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long courseId,
                                                 @PathVariable Long docId,
                                                 HttpServletRequest req) {
        UserResponse user = getCurrentUser(req);
        if (!"TEACHER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.status(403).body("Only teachers can delete documents.");
        }

        documentService.deleteDocument(docId);
        return ResponseEntity.ok("Document deleted.");
    }
}
