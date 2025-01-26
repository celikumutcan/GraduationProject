package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    //Commercial mail to send any specific groups
    @GetMapping("/send-emails")
    public String sendEmails(
            @RequestParam List<String> recipients,
            @RequestParam String subject,
            @RequestParam String body) {
        emailService.sendEmails(recipients, subject, body);
        return "Emails sent successfully to: " + recipients;
    }

    //Only One Person To Send Specific
    @GetMapping("/send-email")
    public String sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {
        emailService.sendEmails(Arrays.asList(to), subject, body);
        return "Email sent successfully to: " + to;
        }
}
