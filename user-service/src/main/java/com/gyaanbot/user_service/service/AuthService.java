package com.gyaanbot.user_service.service;

import com.gyaanbot.user_service.dto.*;
import com.gyaanbot.user_service.entity.Role;
import com.gyaanbot.user_service.entity.Student;
import com.gyaanbot.user_service.entity.Teacher;
import com.gyaanbot.user_service.entity.User;
import com.gyaanbot.user_service.repository.StudentRepository;
import com.gyaanbot.user_service.repository.TeacherRepository;
import com.gyaanbot.user_service.repository.UserRepository;
import com.gyaanbot.user_service.config.JwtUtil;

import com.gyaanbot.user_service.util.OTPGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String otp = OTPGenerator.generateOTP();

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEmailVerified(false);
        user.setOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());


        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);

        return new AuthResponse(null, user.getRole().toString(), user.getEmail()); // no token until verified
    }

    public String verifyOtp(OTPRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEmailVerified()) {
            return "Email already verified";
        }

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Check if OTP expired (10 minutes)
        LocalDateTime generatedTime = user.getOtpGeneratedAt();
        if (generatedTime == null || generatedTime.plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired. Please register again or resend OTP.");
        }

        user.setEmailVerified(true);
        user.setOtp(null);
        user.setOtpGeneratedAt(null);

        // Create empty profile after verification
        if (user.getRole() == Role.STUDENT && user.getStudent() == null) {
            Student student = new Student();
            student.setUser(user);
            studentRepository.save(student);
            user.setStudent(student); // optional but safe
        }

        if (user.getRole() == Role.TEACHER && user.getTeacher() == null) {
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacherRepository.save(teacher);
            user.setTeacher(teacher); // optional but safe
        }

        userRepository.save(user);

        return "Email verified successfully";
    }



    public String resendOtp(@RequestBody ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email is not verified");
        }

        String newOtp = OTPGenerator.generateOTP();
        user.setOtp(newOtp);
        user.setOtpGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), newOtp);

        return "OTP has been resent to your email.";
    }



    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getRole().name(), user.getEmail());
    }


    public String forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email is not verified");
        }

        String otp = OTPGenerator.generateOTP();
        user.setOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
        return "OTP sent to your email to reset password.";
    }


    public String resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Check expiry
        if (user.getOtpGeneratedAt() == null || user.getOtpGeneratedAt().plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired. Please try again.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setOtp(null);
        user.setOtpGeneratedAt(null);
        userRepository.save(user);

        return "Password has been successfully reset.";
    }

}
