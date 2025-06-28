package com.gyaanbot.chatservice.utils;

import com.gyaanbot.chatservice.dto.CourseDocumentDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Slf4j
@Component
public class PdfProcessor {

    public Map<String, String> extractChunks(List<CourseDocumentDTO> docs) {
        Map<String, String> output = new LinkedHashMap<>();

        for (CourseDocumentDTO doc : docs) {
            String url = doc.getUrl();
            String title = doc.getName();
            String text;

            try (InputStream in = new URL(url).openStream();
                 PDDocument pdf = PDDocument.load(in)) {

                PDFTextStripper stripper = new PDFTextStripper();
                text = stripper.getText(pdf);

                if (text.length() > 3000) {
                    text = text.substring(0, 3000) + " ...";
                }

                output.put(title, text.trim());

            } catch (Exception e) {
                output.put(title, "[Error extracting text: " + e.getMessage() + "]");
            }
        }

        return output;
    }
}