package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/emails") // 📌 API için base URL
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * 📧 **Belirli bir gruba e-posta gönderir (Dosya ekleme seçeneği ile)**
     */
    @PostMapping("/send-emails")
    public ResponseEntity<String> sendEmails(
            @RequestParam List<String> recipients,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        emailService.sendEmailWithAttachment(recipients, subject, body, file);
        return ResponseEntity.ok("📧 Emails sent successfully to: " + recipients);
    }

    /**
     * 📧 **Tek bir kişiye e-posta gönderir (Dosya ekleme seçeneği ile)**
     */
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        emailService.sendEmailWithAttachment(Collections.singletonList(to), subject, body, file);
        return ResponseEntity.ok("📧 Email sent successfully to: " + to);
    }
}
