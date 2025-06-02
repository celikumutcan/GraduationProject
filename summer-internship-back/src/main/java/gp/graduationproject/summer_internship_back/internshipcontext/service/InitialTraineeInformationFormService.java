package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InitialTraineeInformationFormDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

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
     * Updates the status of a trainee form.
     * If status is updated to "Company Approval Waiting",
     * a new password is generated, hashed, saved and emailed to the company branch.
     * Also notifies Student Affairs. If status is other, student is notified.
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

                // Step 1: Generate plain password
                String plainPassword = generateRandomPassword();

                // Step 2: Encode the password
                String hashedPassword = passwordResetTokenService.encodePassword(plainPassword);

                // Step 3: Update the user's password in the database
                User branchUser = approvedForm.getCompanyBranch().getBranchUserName();
                branchUser.setPassword(hashedPassword);
                userRepository.save(branchUser);

                // Step 4: Send email with username and plain password
                Optional<CompanyBranch> companyBranchOptional =
                        companyBranchRepository.findByBranchUserName(branchUser);

                if (companyBranchOptional.isPresent()) {
                    CompanyBranch companyBranch = companyBranchOptional.get();

                    emailService.sendCompanyBranchWelcomeEmail(
                            companyBranch.getBranchEmail(),
                            branchUser.getUserName(),
                            plainPassword
                    );
                }
            }

            // Notify Student Affairs
            List<StudentAffair> studentAffairsList = userRepository.findAllStudentAffairs();
            for (StudentAffair sa : studentAffairsList) {
                if (sa.getUsers() != null) {
                    String email = sa.getUsers().getEmail();
                    if (email != null && !email.isBlank()) {
                        String subject = "New Approved Internship Form";
                        String body = "Dear Student Affairs,\n\n" +
                                "A new internship form has been approved. Please log in to the system to review it.\n\n" +
                                "Best regards,\nInternship System";
                        emailService.sendEmail(email, subject, body);
                    }
                }
            }

        } else {
            // Notify student if status is not "Company Approval Waiting"
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


    /**
     * Generates a random 12-character password with special characters.
     */
    private String generateRandomPassword() {
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.-+!@#$%^&*()";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            password.append(charset.charAt(random.nextInt(charset.length())));
        }
        return password.toString();
    }


}