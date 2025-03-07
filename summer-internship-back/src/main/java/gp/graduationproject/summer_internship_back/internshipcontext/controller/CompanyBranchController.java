package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyBranchRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyBranchService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.PasswordResetTokenService;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.PasswordResetToken;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.CompanyBranchDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/company-branch")
public class CompanyBranchController {

    private final CompanyBranchService companyBranchService;
    private final CompanyRepository companyRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private  CompanyBranchRepository companyBranchRepository;  // 🔹 EKLENDİ

    @Autowired
    public CompanyBranchController(CompanyBranchService companyBranchService, CompanyRepository companyRepository, PasswordResetTokenService passwordResetTokenService, UserRepository userRepository) {
        this.companyBranchService = companyBranchService;
        this.companyRepository = companyRepository;
        this.passwordResetTokenService = passwordResetTokenService;
        this.userRepository = userRepository;
        this.companyBranchRepository = companyBranchRepository;  // 🔹 Ataması yapıldı

        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Retrieves all company branches associated with a specific company.
     *
     * @param username The username of the company
     * @return A list of company branches
     */
    @PostMapping
    public ResponseEntity<List<CompanyBranchDTO>> getCompanyBranches(@RequestBody String username) {
        List<CompanyBranch> companyBranches = companyBranchService.getAllCompanyBranchesofCompany(username);
        List<CompanyBranchDTO> companyBranchDTOS = companyBranches.stream()
                .map(branch -> new CompanyBranchDTO(branch.getBranchName()))
                .toList();
        return ResponseEntity.ok(companyBranchDTOS);
    }

    /**
     * Validates the password reset token.
     *
     * @param token The reset token
     * @return Success if valid, error if invalid or expired
     */
    @GetMapping("/validate-reset-token")
    public ResponseEntity<String> validateResetToken(@RequestParam String token) {
        Optional<PasswordResetToken> resetToken = passwordResetTokenService.validatePasswordResetToken(token);
        return resetToken.isPresent() ?
                ResponseEntity.ok("Token is valid.") :
                ResponseEntity.badRequest().body("Invalid or expired token.");
    }

    /**
     * Allows a company branch representative to set a new password.
     *
     * @param requestBody Contains reset token and new password
     * @return Success or error response
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");
        String newPassword = requestBody.get("newPassword");

        if (token == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Token and new password are required.");
        }

        return passwordResetTokenService.validatePasswordResetToken(token).map(resetToken -> {
            String username = resetToken.getUserName();
            Optional<User> userOptional = userRepository.findById(username);

            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found.");
            }

            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            passwordResetTokenService.deletePasswordResetToken(token);
            return ResponseEntity.ok("Password has been successfully reset.");
        }).orElse(ResponseEntity.badRequest().body("Invalid or expired token."));
    }
    @PostMapping("/add")
    @Transactional
    public ResponseEntity<String> addCompanyBranch(@RequestBody Map<String, String> payload) {
        String branchName = payload.get("branch_name");
        String address = payload.get("company_branch_address");
        String email = payload.get("company_branch_email");
        String phone = payload.get("company_branch_phone");
        String country = payload.get("company_branch_country");
        String city = payload.get("company_branch_city");
        String district = payload.get("company_branch_district");

        if (branchName == null || branchName.isEmpty()) {
            return ResponseEntity.status(400).body("Branch name cannot be empty");
        }

        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.setBranchName(branchName);
        companyBranch.setAddress(address);
        companyBranch.setBranchEmail(email);
        companyBranch.setPhone(phone);
        companyBranch.setCountry(country);
        companyBranch.setCity(city);
        companyBranch.setDistrict(district);

        companyBranchRepository.save(companyBranch);
        return ResponseEntity.status(201).body("Company branch added successfully");
    }
}