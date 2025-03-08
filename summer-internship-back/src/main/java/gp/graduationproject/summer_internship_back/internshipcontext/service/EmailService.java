package gp.graduationproject.summer_internship_back.internshipcontext.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;

/**
 * Service class for sending emails, including support for attachments
 * and company branch welcome emails with password reset links.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Constructor for EmailService.
     * Uses constructor injection to initialize JavaMailSender.
     *
     * @param mailSender The mail sender bean used for sending emails.
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an email with optional file attachment.
     *
     * @param recipients List of recipient email addresses.
     * @param subject Email subject.
     * @param body Email content (supports HTML).
     * @param file Optional file attachment.
     */
    public void sendEmailWithAttachment(List<String> recipients, String subject, String body, MultipartFile file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipients.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, true); // Enables HTML format

            // Attach file if provided
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + recipients);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a welcome email with a password reset link to a newly created company branch.
     *
     * @param recipientEmail Email address of the company branch user.
     * @param userName Username for the company branch.
     * @param resetLink Password reset link.
     */
    public void sendCompanyBranchWelcomeEmail(String recipientEmail, String userName, String resetLink) {
        System.out.println("sendCompanyBranchWelcomeEmail method executed.");

        // Validate required fields
        if (recipientEmail == null || recipientEmail.isBlank()) {
            System.err.println("Error: Recipient email is null or empty!");
            return;
        }
        if (userName == null || userName.isBlank()) {
            System.err.println("Error: Username is null or empty!");
            return;
        }
        if (resetLink == null || resetLink.isBlank()) {
            System.err.println("Error: Reset link is null or empty!");
            return;
        }

        System.out.println("Sending email to: " + recipientEmail);
        System.out.println("Username: " + userName);
        System.out.println("Reset Link: " + resetLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("Welcome to the Internship Management System");

            String emailContent = String.format("""
            <p>Dear Company Branch Representative,</p>
            <p>Your account has been successfully created in the Internship Management System.</p>
            <p>Your username: <b>%s</b></p>
            <p>To set your password and complete your registration, please click the link below:</p>
            <p><a href="%s" target="_blank">Set Your Password</a></p>
            <p>If you did not request this, please ignore this email.</p>
            <p>Best regards,<br>Internship Management Team</p>
            """, userName, resetLink);

            helper.setText(emailContent, true);
            mailSender.send(message);

            System.out.println("Email successfully sent to: " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}