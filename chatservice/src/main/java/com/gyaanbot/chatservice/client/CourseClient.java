package com.gyaanbot.chatservice.client;

import com.gyaanbot.chatservice.dto.CourseDocumentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "course-service", url = "${course.service.url}")
public interface CourseClient {

    @GetMapping("/courses/{courseId}/documents")
    List<CourseDocumentDTO> getDocuments(@PathVariable("courseId") Long courseId);
}
