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
     * Sends a welcome email to a company branch with username and plain password.
     *
     * @param recipientEmail Email address of the company branch
     * @param userName The generated username
     * @param plainPassword The generated plain password
     */
    public void sendCompanyBranchWelcomeEmail(String recipientEmail, String userName, String plainPassword) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            System.err.println("Error: recipient email is null or empty.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("Welcome to the Internship System");

            String emailContent = String.format("""
            <p>Dear Company Branch Representative,</p>
            <p>Your account has been created in the Internship Management System.</p>
            <p><b>Username:</b> %s</p>
            <p><b>Password:</b> %s</p>
            <p>Please change your password after your first login.</p>
            <p>Best regards,<br>Internship Management Team</p>
        """, userName, plainPassword);

            helper.setText(emailContent, true);
            mailSender.send(message);

            System.out.println("✅ Mail sent to: " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to: " + recipientEmail);
            e.printStackTrace();
        }
    }


    /**
     * Sends a simple email to a single recipient.
     *
     * @param recipient The recipient email address.
     * @param subject Email subject.
     * @param body Email content.
     */
    public void sendEmail(String recipient, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + recipient);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Sends a password reset email to the company branch with a new password.
     *
     * @param recipientEmail The email address of the company branch
     * @param userName The username of the branch
     * @param plainPassword The new generated password
     */
    public void sendCompanyBranchResetPasswordEmail(String recipientEmail, String userName, String plainPassword) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            System.err.println("Invalid recipient email.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("Your New Password – Internship System");

            String emailContent = String.format("""
            <p>Dear Company Branch,</p>
            <p>You requested a new password for your Internship System account.</p>
            <p><b>Username:</b> %s</p>
            <p><b>New Password:</b> %s</p>
            <p>You can now log in using this password. Please change it after logging in.</p>
            <p>Best regards,<br>Internship Management Team</p>
        """, userName, plainPassword);

            helper.setText(emailContent, true);
            mailSender.send(message);

            System.out.println("✅ Reset password email sent to: " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send reset email: " + recipientEmail);
            e.printStackTrace();
        }
    }

}