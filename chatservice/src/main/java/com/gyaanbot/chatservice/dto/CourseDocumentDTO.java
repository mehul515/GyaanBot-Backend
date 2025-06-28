package com.gyaanbot.chatservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseDocumentDTO {
    private Long id;
    private String name;
    private String description;
    private String url;
    private CourseInfo course;
    private LocalDateTime createdAt;

    public CourseDocumentDTO(Long id, String name, String description, String url, CourseInfo course, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.course = course;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Data
    public static class CourseInfo {
        private Long id;
        private String title;
        private String description;
        private Boolean isPublic;
        private Long teacherId;
        private LocalDateTime createdAt;

        public CourseInfo(Long id, String title, String description, Boolean isPublic, Long teacherId, LocalDateTime createdAt) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.isPublic = isPublic;
            this.teacherId = teacherId;
            this.createdAt = createdAt;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getPublic() {
            return isPublic;
        }

        public void setPublic(Boolean aPublic) {
            isPublic = aPublic;
        }

        public Long getTeacherId() {
            return teacherId;
        }

        public void setTeacherId(Long teacherId) {
            this.teacherId = teacherId;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
