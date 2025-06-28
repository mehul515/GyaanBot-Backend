package com.gyaanbot.user_service.repository;

import com.gyaanbot.user_service.entity.Teacher;
import com.gyaanbot.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUser(User user);
}
