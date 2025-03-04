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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                                  FileStorageService fileStorageService,
                                  AnnouncementRepository announcementRepository) {
        this.academicStaffRepository = academicStaffRepository;
        this.announcementService = announcementService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
        this.announcementRepository = announcementRepository;
    }

    /**
     * Creates a new announcement with title, content, and optional file attachment.
     * Sends the announcement by email to users of a specified type.
     */
    @PostMapping
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestParam("title") String title,
                                                              @RequestParam("content") String content,
                                                              @RequestParam("addUserName") String username,
                                                              @RequestParam("userType") Set<String> userType,
                                                              @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            System.out.println("Mesaj" + title + " "+ content +" "+ username +" "+ userType);
            Optional<AcademicStaff> academicStaffOptional = academicStaffRepository.findByUserName(username);
            if (academicStaffOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            AcademicStaff academicStaff = academicStaffOptional.get();
            User user = userRepository.findByUserName(academicStaff.getUserName());

            // File upload process
            String filePath = null;
            if (file != null && !file.isEmpty()) {
                filePath = fileStorageService.storeFile(file);
            }

            Announcement announcement = new Announcement();
            announcement.setDatetime(Instant.now());
            announcement.setContent(title + "\n\n" + content);
            announcement.setAddUserName(academicStaff);
            announcement.setFilePath(filePath);

            Announcement savedAnnouncement = announcementService.saveAnnouncement(announcement);
            if (savedAnnouncement == null || savedAnnouncement.getId() == null) {
                System.out.println("Error: Announcement not saved!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            else {
                System.out.println("Saved Announcement ID: " + savedAnnouncement.getId());
            }


            List<String> recipientEmails = new ArrayList<>();

            for (String userType1 : userType) {
                recipientEmails.addAll(userRepository.findAllEmailsByUserType(userType1));
            }

            if (!recipientEmails.isEmpty()) {
                if (file != null && !file.isEmpty()) {
                    emailService.sendEmailWithAttachment(recipientEmails,
                            "" + title,
                            content,
                            file
                    );
                } else {
                    emailService.sendEmailWithAttachment(recipientEmails,
                            "" + title,
                            content,
                            null // Send null if no file
                    );
                }
            }

            // Create DTO response
            AnnouncementDTO responseDto = new AnnouncementDTO(title, content, user.getFullName(), savedAnnouncement.getDatetime(), filePath, savedAnnouncement.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves all announcements, including file paths.
     */
    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        List<AnnouncementDTO> announcementDtos = announcementRepository.findAllWithUsers() // Single query!
                .stream()
                .map(announcement -> {
                    AcademicStaff academicStaff = announcement.getAddUserName(); // Already fetched in single query
                    String fullName = academicStaff != null ? academicStaff.getUserName() : "Unknown User";
                    int id = announcement.getId();
                    String[] parts = announcement.getContent().split("\n\n", 2);
                    String title = (parts.length > 0) ? parts[0] : "";
                    String content = (parts.length > 1) ? parts[1] : "";

                    return new AnnouncementDTO(
                            title,
                            content,
                            fullName,
                            announcement.getDatetime(),
                            announcement.getFilePath(),
                            id
                    );
                })
                .toList();

        return ResponseEntity.ok(announcementDtos);
    }

    /**
     * Updates an existing announcement with new title, content, and optional file attachment.
     */
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

            // If file is provided, update file, else keep existing file
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
                    updatedAnnouncement.getFilePath(),
                    updatedAnnouncement.getId()
            );

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Deletes an announcement by its ID.
     */
    @DeleteMapping("/{id}")
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