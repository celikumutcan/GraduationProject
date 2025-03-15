package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InactiveCompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InactiveCompanyBranchRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

/**
 * Service responsible for handling scheduled tasks related to company branches.
 * Sends verification emails once a year and marks inactive branches if not verified.
 */
@Service
public class CompanyBranchSchedulerService {

    private final CompanyBranchRepository companyBranchRepository;
    private final InactiveCompanyBranchRepository inactiveCompanyBranchRepository;
    private final EmailService emailService;

    public CompanyBranchSchedulerService(CompanyBranchRepository companyBranchRepository,
                                         InactiveCompanyBranchRepository inactiveCompanyBranchRepository,
                                         EmailService emailService) {
        this.companyBranchRepository = companyBranchRepository;
        this.inactiveCompanyBranchRepository = inactiveCompanyBranchRepository;
        this.emailService = emailService;
    }

    /**
     * Sends verification emails to all company branches every year on February 27.
     */
    @Scheduled(cron = "0 0 12 27 2 *") // 27 Şubat 12:00'de çalışacak
    @Transactional
    public void sendVerificationEmails() {

        List<CompanyBranch> branches = companyBranchRepository.findAll();

        for (CompanyBranch branch : branches)
        {
            String verificationLink = "https://internship-system.com/verify/" + branch.getId();

            String emailContent = "Dear " + branch.getBranchName() + ",\n\n"
                    + "Please verify your company's activity by clicking the link below:\n"
                    + verificationLink + "\n\n"
                    + "If you do not verify within 30 days, your branch will be marked as inactive.\n\n"
                    + "Best regards,\n"
                    + "Internship Management Team";

            emailService.sendEmail(branch.getBranchEmail(), "Annual Company Branch Verification", emailContent);
        }
    }


    /**
     * Marks company branches as inactive if they do not verify within 30 days.
     * Currently disabled and scheduled for March 28.
     */
    /*
    @Scheduled(cron = "0 0 12 28 3 *") // Runs every year on March 28 at 12:00
    @Transactional
    public void markInactiveBranches() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<CompanyBranch> branches = companyBranchRepository.findAll();

        for (CompanyBranch branch : branches) {
            if (inactiveCompanyBranchRepository.findByBranchId(branch.getId()).isEmpty()) {
                inactiveCompanyBranchRepository.save(new InactiveCompanyBranch(branch.getId(), "No response to verification email"));
            }
        }
    }
    */

    /**
     * Test amaçlı manuel mail gönderimi için kullanılabilir.
     * Varsayılan olarak yorum satırına alınmıştır, demo sırasında açarak kullanabilirsin.
     *
     * ***Postman veya tarayıcıdan nasıl test edilir?***
     * 1. Aşağıdaki kodun yorum satırlarını kaldır.
     * 2. Uygulamayı yeniden başlat.
     * 3. Şu isteği gönder:
     *    **GET http://localhost:8080/api/test/send-mail**
     * 4. Eğer başarılı çalışırsa, şubelere doğrulama maili gönderilir.
     */
    /*
    @RestController
    @RequestMapping("/api/test")
    public class TestController {

        private final CompanyBranchSchedulerService companyBranchSchedulerService;

        public TestController(CompanyBranchSchedulerService companyBranchSchedulerService) {
            this.companyBranchSchedulerService = companyBranchSchedulerService;
        }

        @GetMapping("/send-mail")
        public ResponseEntity<String> triggerEmailManually() {
            companyBranchSchedulerService.sendVerificationEmails();
            return ResponseEntity.ok("Mail gönderme işlemi başlatıldı.");
        }
    }
    */

    /**
     * Demo sırasında manuel olarak bazı şubeleri inaktif yapmak için kullanılabilir.
     * Eğer tekrar çalıştırmak istersen, yorum satırından çıkarıp projeyi başlatabilirsin.
     *
     * Bu fonksiyon sadece bir kere çalışır ve belirtilen şubeleri inaktif hale getirir.
     */
    /*
    @Scheduled(fixedDelay = Long.MAX_VALUE)
    @Transactional
    public void manuallySetInactiveBranches() {
        inactiveCompanyBranchRepository.save(new InactiveCompanyBranch(1, "Test - Manuel inaktive edildi"));
        inactiveCompanyBranchRepository.save(new InactiveCompanyBranch(2, "Test - Manuel inaktive edildi"));
        inactiveCompanyBranchRepository.save(new InactiveCompanyBranch(9, "Test - Manuel inaktive edildi"));
        inactiveCompanyBranchRepository.save(new InactiveCompanyBranch(10, "Test - Manuel inaktive edildi"));

        System.out.println("Test için şubeler manuel olarak inaktif hale getirildi.");
    }
    */
}