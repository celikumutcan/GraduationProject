package gp.graduationproject.summer_internship_back.internshipcontext.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
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
}
