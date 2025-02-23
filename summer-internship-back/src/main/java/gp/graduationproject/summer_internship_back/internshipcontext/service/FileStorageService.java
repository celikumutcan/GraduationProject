package gp.graduationproject.summer_internship_back.internshipcontext.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(fileStorageLocation);
            System.out.println("📂 Dosya klasörü oluşturuldu: " + fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create storage directory!", ex);
        }
    }

    /**
     * 📌 Dosya yükleme işlemi
     */
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("❌ Dosya kaydedilemedi: " + fileName, ex);
        }
    }

    /**
     * 📌 Dosya yükleme işlemi
     */
    public Resource loadFile(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("❌ Dosya bulunamadı: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("❌ Geçersiz dosya yolu: " + fileName, ex);
        }
    }
}
