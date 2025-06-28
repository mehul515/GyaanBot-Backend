package com.gyaanbot.course_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gyaanbot.course_service.dto.DocumentRequest;
import com.gyaanbot.course_service.entity.Course;
import com.gyaanbot.course_service.entity.Document;
import com.gyaanbot.course_service.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private Cloudinary cloudinary;

    public DocumentService(DocumentRepository documentRepository, Cloudinary cloudinary) {
        this.documentRepository = documentRepository;
        this.cloudinary = cloudinary;
    }

    public List<Document> listByCourse(Course course) {
        return documentRepository.findByCourse(course);
    }

    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }

    public Document uploadAndSave(MultipartFile file, String name, String description, Course course) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("secure_url");

            Document document = new Document();
            document.setName(name);
            document.setDescription(description);
            document.setUrl(url);
            document.setCourse(course);
            return documentRepository.save(document);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload document", e);
        }
    }
}
