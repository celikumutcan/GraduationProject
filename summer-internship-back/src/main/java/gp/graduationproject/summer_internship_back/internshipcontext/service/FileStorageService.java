package gp.graduationproject.summer_internship_back.internshipcontext.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
    private final Path studentCvStorageLocation = Paths.get("student_cvs").toAbsolutePath().normalize();

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(fileStorageLocation);
            Files.createDirectories(studentCvStorageLocation);
            System.out.println("📂 Dosya klasörü oluşturuldu: " + fileStorageLocation);
            System.out.println("📂 Öğrenci CV'leri için klasör oluşturuldu: " + studentCvStorageLocation);

        } catch (Exception ex) {
            throw new RuntimeException("Could not create storage directory!", ex);
        }
    }

    /**
     * 📌 Genel dosya yükleme işlemi (Announcements için)
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
     * 📌 Dosya getirme işlemi (Announcements için)
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

    /**
     * 📌 Öğrenci CV yükleme işlemi (Sadece 1 tane CV tutulur)
     */
    public String storeStudentCv(MultipartFile file, String studentUsername) {
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = studentUsername + "_resume" + fileExtension;

        try {
            deleteExistingCv(studentUsername);

            Path targetLocation = studentCvStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("❌ CV kaydedilemedi: " + fileName, ex);
        }
    }

    /**
     * 📌 Öğrenci CV’sini getirir
     */
    public Resource loadStudentCv(String studentUsername) {
        try {
            Path filePath = studentCvStorageLocation.resolve(studentUsername + "_resume.pdf").normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("❌ CV bulunamadı: " + studentUsername);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("❌ Geçersiz dosya yolu!", ex);
        }
    }

    /**
     * 📌 Eğer öğrenci daha önce bir CV yüklediyse, eskiyi sil
     */
    private void deleteExistingCv(String username) {
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(studentCvStorageLocation, username + "_resume.*");
            for (Path file : directoryStream) {
                Files.delete(file);
            }
        } catch (IOException ex) {
            throw new RuntimeException("❌ Eski CV silinemedi: " + username, ex);
        }
    }

    /**
     * 📌 Dosya uzantısını almak için yardımcı metod
     */
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ""; // Uzantıyı boş bırak, hardcoded ".pdf" ekleme
    }
}
