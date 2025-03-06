package gp.graduationproject.summer_internship_back.internshipcontext.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 📧 **Dosya ekleyerek veya ek olmadan e-posta gönderir**
     */
    public void sendEmailWithAttachment(List<String> recipients, String subject, String body, MultipartFile file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Alıcıları ekle
            helper.setTo(recipients.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, true); // HTML formatını destekler

            // 📎 Eğer dosya eklenmişse, ek olarak mail'e ekle
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }

            // E-postayı gönder
            mailSender.send(message);
            System.out.println("📧 Email başarıyla gönderildi: " + recipients);

        } catch (MessagingException e) {
            System.err.println("❌ Email gönderme başarısız: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a welcome email with a password reset link to the newly created company branch.
     *
     * @param recipientEmail Email of the company branch user
     * @param userName Generated username for the company branch
     * @param resetLink Password reset link for setting a new password
     */
    public void sendCompanyBranchWelcomeEmail(String recipientEmail, String userName, String resetLink) {
        System.out.println("📩 sendCompanyBranchWelcomeEmail metodu çalıştı!");

        // Null veya boş değer kontrolü
        if (recipientEmail == null || recipientEmail.isBlank()) {
            System.err.println("❌ Hata: Alıcı email adresi boş veya null!");
            return;
        }
        if (userName == null || userName.isBlank()) {
            System.err.println("❌ Hata: Kullanıcı adı boş veya null!");
            return;
        }
        if (resetLink == null || resetLink.isBlank()) {
            System.err.println("❌ Hata: Reset linki boş veya null!");
            return;
        }

        System.out.println("📩 Gönderilecek Email: " + recipientEmail);
        System.out.println("👤 Kullanıcı Adı: " + userName);
        System.out.println("🔗 Reset Link: " + resetLink);

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

            System.out.println("✅ Email başarıyla gönderildi: " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("❌ Email gönderme hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
}