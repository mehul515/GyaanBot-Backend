package com.gyaanbot.user_service.repository;

import com.gyaanbot.user_service.entity.Student;
import com.gyaanbot.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
}
