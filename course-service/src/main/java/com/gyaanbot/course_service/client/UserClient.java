package com.gyaanbot.course_service.client;

import com.gyaanbot.course_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

    @GetMapping("/user/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);

    @GetMapping("/user/me")
    UserResponse getCurrentUser(@RequestHeader("Authorization") String token);

    @GetMapping("/user/email")
    UserResponse getUserByEmail(
            @RequestParam("email") String email,
            @RequestHeader("Authorization") String token
    );

}
