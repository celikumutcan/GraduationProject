package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyBranchService {

    private final CompanyBranchRepository companyBranchRepository;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;

    @Autowired
    public CompanyBranchService(CompanyBranchRepository companyBranchRepository, EmailService emailService, PasswordResetTokenService passwordResetTokenService) {
        this.companyBranchRepository = companyBranchRepository;
        this.emailService = emailService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    /**
     * Saves a new company branch and sends a welcome email with a password reset link.
     *
     * @param companyBranch The company branch to be saved
     * @return The saved company branch entity
     */
    public CompanyBranch saveCompanyBranch(CompanyBranch companyBranch) {
        System.out.println("🔍 saveCompanyBranch metodu çalıştı!");

        CompanyBranch savedBranch = companyBranchRepository.save(companyBranch);

        // Generate and store a password reset token
        String userName = savedBranch.getBranchUserName().getUserName();
        String resetToken = passwordResetTokenService.createPasswordResetToken(userName);
        String resetLink = "https://your-app.com/reset-password?token=" + resetToken;

        // Debug logları ekleyelim
        System.out.println("🔍 Email gönderme fonksiyonu çağrılmak üzere!");
        System.out.println("📧 Alıcı Email: " + savedBranch.getBranchEmail());
        System.out.println("👤 Kullanıcı Adı: " + userName);
        System.out.println("🔗 Reset Link: " + resetLink);

        System.out.println("✅ Email gönderme fonksiyonu çağrıldı!");
        // Send welcome email with the password reset link
        try {
            emailService.sendCompanyBranchWelcomeEmail(
                    savedBranch.getBranchEmail(),
                    userName,
                    resetLink
            );

        } catch (Exception e) {
            System.out.println("❌ Email gönderme başarısız oldu: " + e.getMessage());
            companyBranchRepository.delete(savedBranch); // Eğer e-posta gitmezse kaydı geri al
            throw new RuntimeException("Failed to send email. Registration aborted.", e);
        }

        return savedBranch;
    }

    public List<CompanyBranch> getAllCompanyBranchesofCompany(String userName) {
        return companyBranchRepository.findAllByCompanyUserName_UserName(userName);
    }

    public Optional<CompanyBranch> getCompanyBranchById(Integer id) {
        return companyBranchRepository.findById(id);
    }

    public void deleteCompany(Integer id) {
        companyBranchRepository.deleteById(id);
    }
}