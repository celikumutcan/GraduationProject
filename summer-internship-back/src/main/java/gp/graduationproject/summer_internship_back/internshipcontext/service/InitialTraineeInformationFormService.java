package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.PasswordResetTokenService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InitialTraineeInformationFormService {

    private final InitialTraineeInformationFormRepository initialTraineeInformationFormRepository;
    private final StudentRepository studentRepository;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final PasswordResetTokenService passwordResetTokenService;

    /**
     * Constructor for InitialTraineeInformationFormService.
     * Injects dependencies for handling trainee forms.
     */
    public InitialTraineeInformationFormService(
            InitialTraineeInformationFormRepository initialTraineeInformationFormRepository,
            StudentRepository studentRepository,
            ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,
            PasswordResetTokenService passwordResetTokenService)
    {
        this.initialTraineeInformationFormRepository = initialTraineeInformationFormRepository;
        this.studentRepository = studentRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    public List<InitialTraineeInformationForm> getAllInitialTraineeInformationFormOfStudent(String userName)
    {
        studentRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return initialTraineeInformationFormRepository.findAllByFillUserName_UserName(userName);
    }

    public List<InitialTraineeInformationForm> getInitialTraineeInformationForms()
    {
        return initialTraineeInformationFormRepository.findAll();
    }

    public Optional<InitialTraineeInformationForm> getInitialTraineeInformationFormById(Integer id)
    {
        return initialTraineeInformationFormRepository.findById(id);
    }

    @Transactional
    public boolean deleteInitialTraineeInformationForm(Integer id, String username) {
        Optional<InitialTraineeInformationForm> form = initialTraineeInformationFormRepository.findById(id);

        if (form.isPresent() && form.get().getFillUserName().getUserName().equals(username)) {
            initialTraineeInformationFormRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Transactional
    public InitialTraineeInformationForm updateInitialTraineeInformationForm(Integer id, String username, InitialTraineeInformationForm updatedForm)
    {
        InitialTraineeInformationForm form = initialTraineeInformationFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Form not found! ID: " + id));

        if (!form.getFillUserName().getUserName().equals(username)) {
            throw new RuntimeException("Unauthorized action! User " + username + " is not the owner of this form.");
        }

        form.setPosition(updatedForm.getPosition());
        form.setType(updatedForm.getType());
        form.setCode(updatedForm.getCode());
        form.setSemester(updatedForm.getSemester());
        form.setSupervisorName(updatedForm.getSupervisorName());
        form.setSupervisorSurname(updatedForm.getSupervisorSurname());
        form.setHealthInsurance(updatedForm.getHealthInsurance());
        form.setStatus(updatedForm.getStatus());
        form.setCompanyUserName(updatedForm.getCompanyUserName());
        form.setBranchName(updatedForm.getBranchName());
        form.setCompanyBranchAddress(updatedForm.getCompanyBranchAddress());
        form.setCompanyBranchPhone(updatedForm.getCompanyBranchPhone());
        form.setCompanyBranchEmail(updatedForm.getCompanyBranchEmail());
        form.setInternshipStartDate(updatedForm.getInternshipStartDate());
        form.setInternshipEndDate(updatedForm.getInternshipEndDate());
        form.setCountry(updatedForm.getCountry());
        form.setCity(updatedForm.getCity());
        form.setDistrict(updatedForm.getDistrict());

        return initialTraineeInformationFormRepository.save(form);
    }

    /**
     * Updates the status of an initial trainee information form.
     * If the status is updated to "Company Approval Waiting", a password reset token is created.
     */
    @Transactional
    public boolean updateInitialFormStatus(Integer id, String status) {
        Optional<InitialTraineeInformationForm> optionalForm = initialTraineeInformationFormRepository.findById(id);

        if (optionalForm.isEmpty()) {
            return false; // Eğer kayıt bulunamazsa işlemi iptal et
        }

        InitialTraineeInformationForm form = optionalForm.get();
        initialTraineeInformationFormRepository.updateStatus(id, status);

        if ("Company Approval Waiting".equals(status)) {
            Optional<ApprovedTraineeInformationForm> approvedFormOptional =
                    approvedTraineeInformationFormRepository.findTopByFillUserName_UserNameOrderByIdDesc(
                            form.getFillUserName().getUserName()
                    );

            if (approvedFormOptional.isPresent()) {
                ApprovedTraineeInformationForm approvedForm = approvedFormOptional.get();
                String resetToken = passwordResetTokenService.createPasswordResetToken(
                        approvedForm.getCompanyBranch().getBranchUserName().getUserName()
                );
                System.out.println("✅ Password reset token created: " + resetToken);
            } else {
                System.out.println("❌ Approved Table’da uygun bir kayıt bulunamadı!");
            }
        }
        return true;
    }

}