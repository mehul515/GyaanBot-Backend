package com.gyaanbot.chatservice.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfTextExtractor {

    public static String extractTextFromUrl(String pdfUrl) {
        try {
            URL url = new URL(pdfUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // Fake browser

            try (InputStream in = conn.getInputStream();
                 PDDocument document = PDDocument.load(in)) {

                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }

        } catch (Exception e) {
            return "[Error extracting text: " + e.getMessage() + "]";
        }
    }

}
