package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Announcement;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.AnnouncementService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.AnnouncementDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AcademicStaffRepository academicStaffRepository;
    private final AnnouncementService announcementService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public AnnouncementController(AcademicStaffRepository academicStaffRepository,
                                  AnnouncementService announcementService,
                                  UserRepository userRepository,
                                  EmailService emailService) {
        this.academicStaffRepository = academicStaffRepository;
        this.announcementService = announcementService;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestBody Map<String, String> payload) {
        System.out.println("Create Announcement has been invoked");
        String title = payload.get("title");
        String content = payload.get("content");
        String username = payload.get("addUserName");

        // Validate payload
        if (title == null || content == null || username == null) {
            return ResponseEntity.badRequest().body(null); // All fields are mandatory
        }

        // Retrieve AcademicStaff
        Optional<AcademicStaff> academicStaffOptional = academicStaffRepository.findByUserName(username);
        if (academicStaffOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // AcademicStaff not found
        }

        AcademicStaff academicStaff = academicStaffOptional.get();
        User user = userRepository.findByUserName(academicStaff.getUserName());

        // Create and save Announcement
        Announcement announcement = new Announcement();
        announcement.setDatetime(Instant.now());
        announcement.setContent(title + "\n\n" + content);
        announcement.setAddUserName(academicStaff);

        Announcement savedAnnouncement = announcementService.saveAnnouncement(announcement);

        // Send notification emails
        List<String> studentEmails = userRepository.findAllEmailsByUserType("student");
        emailService.sendEmails(studentEmails, "New Announcement: " + title, content);

        // Return created announcement as DTO
        AnnouncementDTO responseDto = new AnnouncementDTO(
                title,
                content,
                user.getFullName(),
                savedAnnouncement.getDatetime()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        List<AnnouncementDTO> announcementDtos = announcementService.getAllAnnouncements()
                .stream()
                .map(announcement -> {
                    AcademicStaff academicStaff = announcement.getAddUserName();

                    User user = userRepository.findByUserName(academicStaff.getUserName());
                    // Split content into title and content
                    String[] parts = announcement.getContent().split("\n\n", 2);
                    String title = parts.length > 0 ? parts[0] : "";
                    String content = parts.length > 1 ? parts[1] : "";

                    return new AnnouncementDTO(
                            title,
                            content,
                            user.getFullName(),
                            announcement.getDatetime()
                    );
                })
                .toList();

        return ResponseEntity.ok(announcementDtos);
    }
}
