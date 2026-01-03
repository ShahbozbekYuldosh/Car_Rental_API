package com.carrental.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public String saveImage(MultipartFile file, String category) {
        if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Faqat rasm yuklash mumkin!");
        }

        try {
            LocalDate now = LocalDate.now();
            String datePath = String.format("%d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            Path finalDir = Paths.get(uploadDir, datePath, category);

            if (!Files.exists(finalDir)) {
                Files.createDirectories(finalDir);
            }

            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + extension;
            Path targetPath = finalDir.resolve(fileName);

            try (var inputStream = file.getInputStream()) {
                Thumbnails.of(inputStream)
                        .size(1920, 1080)
                        .outputQuality(0.8)
                        .toFile(targetPath.toFile());
            }

            log.info("Fayl muvaffaqiyatli saqlandi: {}", targetPath);
            return datePath + "/" + category + "/" + fileName;

        } catch (IOException e) {
            log.error("Fayl saqlashda xato: {}", e.getMessage());
            throw new RuntimeException("Faylni saqlashda xatolik yuz berdi");
        }
    }

    private String getFileExtension(String fileName) {
        return fileName != null && fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf(".") + 1) : "png";
    }

    public void deleteFile(String relativePath) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(relativePath);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Faylni o'chirishda xatolik: {}", e.getMessage());
        }
    }
}