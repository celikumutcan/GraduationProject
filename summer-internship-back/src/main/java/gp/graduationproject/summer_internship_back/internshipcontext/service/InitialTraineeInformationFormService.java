package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InitialTraineeInformationFormDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.StudentAffair;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing trainee information forms.
 */
@Service
public class InitialTraineeInformationFormService {

    private final InitialTraineeInformationFormRepository initialTraineeInformationFormRepository;
    private final StudentRepository studentRepository;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final PasswordResetTokenService passwordResetTokenService;
    private final CompanyBranchRepository companyBranchRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;


    /**
     * Constructor for dependency injection.
     *
     * @param initialTraineeInformationFormRepository Repository for initial trainee forms
     * @param studentRepository Repository for student data
     * @param approvedTraineeInformationFormRepository Repository for approved trainee forms
     * @param passwordResetTokenService Service for password reset token management
     * @param companyBranchRepository Repository for company branch data
     * @param emailService Service for sending emails
     */
    public InitialTraineeInformationFormService(
            InitialTraineeInformationFormRepository initialTraineeInformationFormRepository,
            StudentRepository studentRepository,
            ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,
            PasswordResetTokenService passwordResetTokenService,
            CompanyBranchRepository companyBranchRepository,
            EmailService emailService,
            UserRepository userRepository
    ) {
        this.initialTraineeInformationFormRepository = initialTraineeInformationFormRepository;
        this.studentRepository = studentRepository;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.passwordResetTokenService = passwordResetTokenService;
        this.companyBranchRepository = companyBranchRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all trainee forms associated with a student.
     *
     * @param userName The username of the student
     * @return List of initial trainee forms
     */
    public List<InitialTraineeInformationForm> getAllInitialTraineeInformationFormOfStudent(String userName) {
        studentRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return initialTraineeInformationFormRepository.findAllByFillUserName_UserName(userName);
    }

    /**
     * Retrieves all initial trainee forms.
     *
     * @return List of initial trainee forms
     */
    public List<InitialTraineeInformationForm> getInitialTraineeInformationForms() {
        return initialTraineeInformationFormRepository.findAll();
    }

    /**
     * Retrieves a trainee form by its ID.
     *
     * @param id The ID of the trainee form
     * @return Optional containing the trainee form if found
     */
    public Optional<InitialTraineeInformationForm> getInitialTraineeInformationFormById(Integer id) {
        return initialTraineeInformationFormRepository.findById(id);
    }

    /**
     * Deletes a trainee form if it belongs to the specified user.
     *
     * @param id ID of the trainee form
     * @param username Username of the student
     * @return true if deleted, false otherwise
     */
    @Transactional
    public boolean deleteInitialTraineeInformationForm(Integer id, String username) {
        Optional<InitialTraineeInformationForm> form = initialTraineeInformationFormRepository.findById(id);

        if (form.isPresent() && form.get().getFillUserName().getUserName().equals(username)) {
            initialTraineeInformationFormRepository.deleteById(id);
            return true;
        }

        return false;
    }

    /**
     * Updates a trainee form if it belongs to the specified user.
     *
     * @param id ID of the trainee form
     * @param username Username of the student
     * @param updatedForm The updated form data
     * @return The updated trainee form entity
     */
    @Transactional
    public InitialTraineeInformationForm updateInitialTraineeInformationForm(Integer id, String username, InitialTraineeInformationForm updatedForm) {
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
     * Updates the status of a trainee form. If status is updated to "Company Approval Waiting",
     * a password reset token is created and an email is sent to the company branch.
     * Also notifies Student Affairs. If status is other (e.g., "Rejected", "Approved"), student is notified.
     *
     * @param id ID of the trainee form
     * @param status New status to be assigned
     * @return true if the update was successful, false otherwise
     */
    @Transactional
    public boolean updateInitialFormStatus(Integer id, String status) {
        Optional<InitialTraineeInformationForm> optionalForm = initialTraineeInformationFormRepository.findById(id);

        if (optionalForm.isEmpty()) {
            return false;
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

                Optional<CompanyBranch> companyBranchOptional = companyBranchRepository
                        .findByBranchUserName(approvedForm.getCompanyBranch().getBranchUserName());

                if (companyBranchOptional.isPresent()) {
                    CompanyBranch companyBranch = companyBranchOptional.get();
                    String resetLink = "https://your-app.com/reset-password?token=" + resetToken;

                    emailService.sendCompanyBranchWelcomeEmail(
                            companyBranch.getBranchEmail(),
                            companyBranch.getBranchUserName().getUserName(),
                            resetLink
                    );
                }
            }

            List<StudentAffair> studentAffairsList = userRepository.findAllStudentAffairs();
            System.out.println("Student Affairs list size: " + studentAffairsList.size());

            for (StudentAffair sa : studentAffairsList) {
                if (sa.getUsers() != null) {
                    String email = sa.getUsers().getEmail();
                    System.out.println("Checking StudentAffair: " + sa.getUserName());

                    if (email != null && !email.isBlank()) {
                        System.out.println("Sending email to Student Affairs: " + email);
                        String subject = "New Approved Internship Form";
                        String body = "Dear Student Affairs,\n\n" +
                                "A new internship form has been approved. Please log in to the system to review it.\n\n" +
                                "Best regards,\nInternship System";
                        emailService.sendEmail(email, subject, body);
                    } else {
                        System.out.println("Email not found for StudentAffair: " + sa.getUserName());
                    }
                } else {
                    System.out.println("User entity is null for StudentAffair: " + sa.getUserName());
                }
            }

        } else {
            String studentEmail = form.getFillUserName().getUsers().getEmail();
            if (studentEmail != null && !studentEmail.isBlank()) {
                String subject = "Your Internship Form Status Has Been Updated";
                String body = "Dear Student,\n\n" +
                        "The status of your internship form has been updated to: " + status + ".\n\n" +
                        "Kind regards,\nInternship Management System";
                emailService.sendEmail(studentEmail, subject, body);
            }
        }

        return true;
    }


    /**
     * Returns all initial trainee forms as DTOs for the given student.
     *
     * @param username student’s username
     * @return list of InitialTraineeInformationFormDTO
     */
    public List<InitialTraineeInformationFormDTO> getAllInitialFormDTOsByStudent(String username) {
        return initialTraineeInformationFormRepository.findAllInitialFormDTOsByStudentUsername(username);
    }

    /**
     * Retrieves all initial trainee information forms mapped directly to DTOs for coordinator use.
     */
    public List<InitialTraineeInformationFormDTO> getAllInitialTraineeFormDTOsForCoordinator() {
        return initialTraineeInformationFormRepository.findAllInitialTraineeInformationFormDTOs();
    }

}