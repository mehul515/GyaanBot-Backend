package com.gyaanbot.user_service.service;

import com.gyaanbot.user_service.dto.StudentProfileRequest;
import com.gyaanbot.user_service.dto.StudentProfileResponse;
import com.gyaanbot.user_service.entity.Role;
import com.gyaanbot.user_service.entity.Student;
import com.gyaanbot.user_service.entity.User;
import com.gyaanbot.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StudentService {
    @Autowired
    private UserRepository userRepository;

    public StudentProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.STUDENT)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a student");

        Student s = user.getStudent();

        StudentProfileResponse res = new StudentProfileResponse();
        res.setName(s.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setRollNumber(s.getRollNumber());
        res.setDepartment(s.getDepartment());
        res.setYearOfStudy(s.getYearOfStudy());
        res.setSection(s.getSection());
        res.setContactNumber(s.getContactNumber());
        res.setAddress(s.getAddress());
        res.setDob(s.getDateOfBirth() != null ? s.getDateOfBirth().toString() : null);
        res.setCollege(s.getCollege());
        res.setGender(s.getGender());

        return res;
    }

    public void updateProfile(String email, StudentProfileRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.STUDENT)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a student");

        Student s = user.getStudent();
        if (s == null) {
            s = new Student();
            s.setUser(user);
            user.setStudent(s);
        }

        s.setName(req.getName());
        s.setRollNumber(req.getRollNumber());
        s.setDepartment(req.getDepartment());
        s.setYearOfStudy(req.getYearOfStudy());
        s.setSection(req.getSection());
        s.setContactNumber(req.getContactNumber());
        s.setAddress(req.getAddress());
        s.setCollege(req.getCollege());
        s.setGender(req.getGender());

        if (req.getDob() != null && !req.getDob().isBlank())
            s.setDateOfBirth(LocalDate.parse(req.getDob()));

        userRepository.save(user);
    }
}
