package com.gyaanbot.user_service.service;

import com.gyaanbot.user_service.dto.TeacherProfileRequest;
import com.gyaanbot.user_service.dto.TeacherProfileResponse;
import com.gyaanbot.user_service.entity.Teacher;
import com.gyaanbot.user_service.entity.User;
import com.gyaanbot.user_service.entity.Role;
import com.gyaanbot.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TeacherService {

    @Autowired
    private UserRepository userRepository;

    public TeacherProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.TEACHER)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a teacher");

        Teacher t = user.getTeacher();

        TeacherProfileResponse res = new TeacherProfileResponse();
        res.setName(t.getName());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setEmployeeId(t.getEmployeeId());
        res.setDepartment(t.getDepartment());
        res.setDesignation(t.getDesignation());
        res.setContactNumber(t.getContactNumber());
        res.setAddress(t.getAddress());
        res.setDob(t.getDateOfBirth() != null ? t.getDateOfBirth().toString() : null);
        res.setBio(t.getBio());
        res.setInstitution(t.getInstitution());
        res.setQualification(t.getQualification());

        return res;
    }

    public void updateProfile(String email, TeacherProfileRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != Role.TEACHER)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not a teacher");

        Teacher t = user.getTeacher();
        if (t == null) {
            t = new Teacher();
            t.setUser(user);
            user.setTeacher(t);
        }

        t.setName(req.getName());
        t.setEmployeeId(req.getEmployeeId());
        t.setDepartment(req.getDepartment());
        t.setDesignation(req.getDesignation());
        t.setContactNumber(req.getContactNumber());
        t.setAddress(req.getAddress());
        t.setBio(req.getBio());
        t.setInstitution(req.getInstitution());
        t.setQualification(req.getQualification());

        if (req.getDob() != null && !req.getDob().isBlank())
            t.setDateOfBirth(LocalDate.parse(req.getDob()));

        userRepository.save(user);
    }
}
