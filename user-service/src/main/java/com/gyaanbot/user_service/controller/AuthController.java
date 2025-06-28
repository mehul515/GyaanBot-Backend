package com.gyaanbot.user_service.controller;

import com.gyaanbot.user_service.dto.*;
import com.gyaanbot.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    // ✅ Register user
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        System.out.println("Login Request : " + request.getEmail() + " " + request.getPassword());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // ✅ Verify OTP
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OTPRequest request) {
        String result = authService.verifyOtp(request);
        return ResponseEntity.ok(result);
    }

    // ✅ Resend OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody ForgotPasswordRequest request) {
        String result = authService.resendOtp(request);
        return ResponseEntity.ok(result);
    }

    // ✅ Forgot password (send OTP)
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String result = authService.forgotPassword(request);
        return ResponseEntity.ok(result);
    }

    // ✅ Reset password (submit OTP + new password)
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        String result = authService.resetPassword(request);
        return ResponseEntity.ok(result);
    }
}
