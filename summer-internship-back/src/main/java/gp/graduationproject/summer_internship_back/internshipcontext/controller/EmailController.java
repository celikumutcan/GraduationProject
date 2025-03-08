package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

/**
 * Controller for handling email-related operations.
 */
@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Sends an email to multiple recipients with an optional attachment.
     *
     * @param recipients List of recipient email addresses
     * @param subject Email subject
     * @param body Email content
     * @param file Optional file attachment
     * @return Response indicating success or failure
     */
    @PostMapping("/send-emails")
    public ResponseEntity<String> sendEmails(
            @RequestParam List<String> recipients,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        emailService.sendEmailWithAttachment(recipients, subject, body, file);
        return ResponseEntity.ok("Emails sent successfully to: " + recipients);
    }

    /**
     * Sends an email to a single recipient with an optional attachment.
     *
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Email content
     * @param file Optional file attachment
     * @return Response indicating success or failure
     */
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        emailService.sendEmailWithAttachment(Collections.singletonList(to), subject, body, file);
        return ResponseEntity.ok("Email sent successfully to: " + to);
    }
}