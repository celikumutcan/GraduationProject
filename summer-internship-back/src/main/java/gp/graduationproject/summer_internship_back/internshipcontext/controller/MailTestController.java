/**package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail-test")
public class MailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public ResponseEntity<String> sendTestEmail() {
        try {
            emailService.sendCompanyBranchWelcomeEmail(
                    "celik.umutcan@outlook.com",
                    "TestUser",
                    "https://your-app.com/reset-password?token=TEST123"
            );
            return ResponseEntity.ok("✅ Test maili başarıyla gönderildi!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Hata oluştu: " + e.getMessage());
        }
    }
}
*/