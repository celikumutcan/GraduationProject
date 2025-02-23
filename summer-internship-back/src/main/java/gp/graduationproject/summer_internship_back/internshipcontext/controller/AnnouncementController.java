package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Announcement;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AnnouncementRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.AnnouncementService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.FileStorageService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.AnnouncementDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AcademicStaffRepository academicStaffRepository;
    private final AnnouncementService announcementService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;
    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementController(AcademicStaffRepository academicStaffRepository,
                                  AnnouncementService announcementService,
                                  UserRepository userRepository,
                                  EmailService emailService,
                                  FileStorageService fileStorageService, AnnouncementRepository announcementRepository) {
        this.academicStaffRepository = academicStaffRepository;
        this.announcementService = announcementService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
        this.announcementRepository = announcementRepository;

    }

    /**
     * 📌 Yeni bir duyuru oluşturur, istenirse dosya ekleyerek kaydeder.
     */
    @PostMapping
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestParam("title") String title,
                                                              @RequestParam("content") String content,
                                                              @RequestParam("addUserName") String username,
                                                              @RequestParam("userType") String userType, // 📌 Kullanıcı tipi seçimi
                                                              @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Optional<AcademicStaff> academicStaffOptional = academicStaffRepository.findByUserName(username);
            if (academicStaffOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            AcademicStaff academicStaff = academicStaffOptional.get();
            User user = userRepository.findByUserName(academicStaff.getUserName());

            // 📌 **Dosya yükleme işlemi**
            String filePath = null;
            if (file != null && !file.isEmpty()) {
                filePath = fileStorageService.storeFile(file);  // Dosya sistemine kaydet
            }

            Announcement announcement = new Announcement();
            announcement.setDatetime(Instant.now());
            announcement.setContent(title + "\n\n" + content);
            announcement.setAddUserName(academicStaff);
            announcement.setFilePath(filePath); // 📎 Dosya yolu ekleniyor

            Announcement savedAnnouncement = announcementService.saveAnnouncement(announcement);

            // 📧 **Mail Gönderme İşlemi**
            List<String> recipientEmails = userRepository.findAllEmailsByUserType(userType); // 📌 Seçilen userType'a göre çek

            if (!recipientEmails.isEmpty()) {
                if (file != null && !file.isEmpty()) {
                    emailService.sendEmailWithAttachment(recipientEmails,
                            "" + title,
                            content,
                            file // **Dosya varsa gönder**
                    );
                } else {
                    emailService.sendEmailWithAttachment(recipientEmails,
                            "" + title,
                            content,
                            null // **Dosya yoksa null olarak gönder**
                    );
                }
            }

            // DTO oluştur
            AnnouncementDTO responseDto = new AnnouncementDTO(title, content, user.getFullName(), savedAnnouncement.getDatetime(), filePath);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 📌 Tüm duyuruları getirir, dosya bağlantılarını da içerir.
     */
    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        List<AnnouncementDTO> announcementDtos = announcementRepository.findAllWithUsers() // Tek sorgu!
                .stream()
                .map(announcement -> {
                    AcademicStaff academicStaff = announcement.getAddUserName(); // Zaten tek sorguda geldi!
                    String fullName = academicStaff != null ? academicStaff.getUserName() : "Unknown User";

                    String[] parts = announcement.getContent().split("\n\n", 2);
                    String title = (parts.length > 0) ? parts[0] : "";
                    String content = (parts.length > 1) ? parts[1] : "";

                    return new AnnouncementDTO(
                            title,
                            content,
                            fullName,
                            announcement.getDatetime(),
                            announcement.getFilePath()
                    );
                })
                .toList();

        return ResponseEntity.ok(announcementDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(
            @PathVariable Integer id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Optional<Announcement> announcementOptional = announcementService.findById(id);
            if (announcementOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Announcement announcement = announcementOptional.get();
            announcement.setContent(title + "\n\n" + content);

            // 📌 **Dosya varsa güncelle, yoksa eskisini koru**
            if (file != null && !file.isEmpty()) {
                String filePath = fileStorageService.storeFile(file);
                announcement.setFilePath(filePath);
            }

            Announcement updatedAnnouncement = announcementService.saveAnnouncement(announcement);

            AnnouncementDTO responseDto = new AnnouncementDTO(
                    title,
                    content,
                    updatedAnnouncement.getAddUserName().getUsers().getFullName(),
                    updatedAnnouncement.getDatetime(),
                    updatedAnnouncement.getFilePath()
            );

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    public ResponseEntity<String> deleteAnnouncement(@PathVariable Integer id) {
        try {
            boolean isDeleted = announcementService.deleteAnnouncement(id);
            if (!isDeleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Announcement not found");
            }
            return ResponseEntity.ok("Announcement deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting announcement");
        }
    }
}
