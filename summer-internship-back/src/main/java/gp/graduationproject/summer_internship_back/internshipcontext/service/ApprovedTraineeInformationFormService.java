package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.*;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Service class for handling operations related to approved trainee information forms.
 */
@Service
public class ApprovedTraineeInformationFormService {

    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final StudentRepository studentRepository;
    private final AcademicStaffRepository academicStaffRepository;
    private final InsuranceApprovalLogRepository insuranceApprovalLogRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;


    /**
     * Constructs an instance of the service with required dependencies.
     *
     * @param approvedTraineeInformationFormRepository Repository for accessing approved trainee forms.
     * @param studentRepository Repository for accessing student information.
     * @param academicStaffRepository Repository for accessing academic staff.
     * @param insuranceApprovalLogRepository Repository for logging insurance approval actions.
     */
    @Autowired
    public ApprovedTraineeInformationFormService(
            ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,
            StudentRepository studentRepository,
            AcademicStaffRepository academicStaffRepository,
            InsuranceApprovalLogRepository insuranceApprovalLogRepository,EmailService emailService,UserRepository userRepository) {
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.studentRepository = studentRepository;
        this.academicStaffRepository = academicStaffRepository;
        this.insuranceApprovalLogRepository = insuranceApprovalLogRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }


    /**
     * Retrieves all approved trainee information forms.
     *
     * @return List of all approved trainee forms.
     */
    public List<ApprovedTraineeInformationForm> getApprovedTraineeInformationForms() {
        return approvedTraineeInformationFormRepository.findAll();
    }

    /**
     * Retrieves a single approved trainee information form by ID.
     *
     * @param id The ID of the approved trainee form.
     * @return The approved trainee form, if found.
     */
    @Transactional
    public Optional<ApprovedTraineeInformationForm> getApprovedTraineeInformationFormById(@NonNull Integer id) {
        return approvedTraineeInformationFormRepository.findById(id);
    }


    /**
     * Retrieves all approved trainee information forms associated with a specific company branch.
     *
     * @param id The ID of the company branch.
     * @return List of trainee forms related to the company branch.
     */
    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormOfCompany(Integer id) {
        return approvedTraineeInformationFormRepository.findAllByCompanyBranch_Id(id);
    }

    /**
     * Approves the insurance for a specific internship and logs the approval.
     *
     * @param internshipId The ID of the internship.
     * @param approvedBy The username of the student affairs officer who approved the insurance.
     */
    @Transactional
    public void approveInsurance(@NonNull Integer internshipId, @NonNull String approvedBy) {

        ApprovedTraineeInformationForm internship = approvedTraineeInformationFormRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));


        internship.setInsuranceApproval(true);
        internship.setInsuranceApprovalDate(LocalDate.now());
        approvedTraineeInformationFormRepository.save(internship);


        InsuranceApprovalLog log = new InsuranceApprovalLog();
        log.setApprovedTraineeId(internship.getId());
        log.setStudentUserName(internship.getFillUserName().getUserName());
        log.setCompanyBranchId(internship.getCompanyBranch().getId());
        log.setInternshipStartDate(internship.getInternshipStartDate());
        log.setInternshipEndDate(internship.getInternshipEndDate());
        log.setBranchAddress(internship.getCompanyBranch().getAddress());
        log.setHealthInsurance(internship.getHealthInsurance());
        log.setApprovalDate(LocalDateTime.now());
        log.setApprovedBy(approvedBy);
        insuranceApprovalLogRepository.save(log);

        User student = userRepository.findByUserName(internship.getFillUserName().getUserName());
        if (student != null && student.getEmail() != null && !student.getEmail().isBlank()) {
            String subject = "Your Internship Insurance Has Been Approved";
            String body = "Dear " + student.getUserName() + ",\n\n" +
                    "Your internship insurance has been successfully approved.\n\n" +
                    "Best regards,\nInternship Management System";
            emailService.sendEmail(student.getEmail(), subject, body);
        }
    }


    /**
     * Approves an internship, assigns coordinator and evaluator if needed, and notifies the student.
     *
     * @param internshipId The ID of the internship to be approved.
     */
    @Transactional
    public void approveInternship(@NonNull Integer internshipId) {
        ApprovedTraineeInformationForm internship = approvedTraineeInformationFormRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        internship.setStatus("Approved");

        // Assign a coordinator if not already set
        if (internship.getCoordinatorUserName() == null) {
            List<AcademicStaff> coordinators = academicStaffRepository.findByCflagTrue();
            if (!coordinators.isEmpty()) {
                internship.setCoordinatorUserName(coordinators.get(new Random().nextInt(coordinators.size())));
            }
        }

        // Assign an evaluating faculty member if not already set
        if (internship.getEvaluatingFacultyMember() == null) {
            List<AcademicStaff> evaluators = academicStaffRepository.findByIflagTrue();
            if (!evaluators.isEmpty()) {
                internship.setEvaluatingFacultyMember(evaluators.get(new Random().nextInt(evaluators.size())).getUserName());
            }
        }

        approvedTraineeInformationFormRepository.save(internship);

        // Send email to student after approval
        User student = userRepository.findByUserName(internship.getFillUserName().getUserName());
        if (student != null && student.getEmail() != null && !student.getEmail().isBlank()) {
            String subject = "Your Internship Has Been Approved by the Company";
            String body = "Dear Student,\n\n" +
                    "Your internship has been approved by the company.\n\n" +
                    "Kind regards,\nInternship Management System";
            emailService.sendEmail(student.getEmail(), subject, body);
        }
    }


    @Transactional
    public boolean updateFormStatus(Integer id, String status) {
        Optional<ApprovedTraineeInformationForm> formOptional = approvedTraineeInformationFormRepository.findById(id);

        if (formOptional.isPresent()) {
            ApprovedTraineeInformationForm form = formOptional.get();
            form.setStatus(status);
            approvedTraineeInformationFormRepository.save(form);
            return true;
        }
        return false;
    }

    /**
     * Deletes an approved trainee information form by its ID if the requesting user owns it.
     *
     * @param id       The ID of the form to be deleted.
     * @param username The username of the user requesting the deletion.
     * @return true if the deletion is successful.
     * @throws RuntimeException if the form is not found or the user is unauthorized.
     */
    @Transactional
    public boolean deleteApprovedTraineeInformationForm(Integer id, String username) {
        Optional<ApprovedTraineeInformationForm> form = approvedTraineeInformationFormRepository.findById(id);

        if (form.isEmpty()) {
            throw new RuntimeException("Error: Form not found! ID: " + id);
        }

        // Check if the user is the owner of the form
        if (!form.get().getFillUserName().getUserName().equals(username)) {
            throw new RuntimeException("Unauthorized action! User " + username + " is not the owner of this form.");
        }

        approvedTraineeInformationFormRepository.deleteById(id);
        System.out.println("Deletion successful. Form ID: " + id);
        return true;
    }

    /**
     * Updates an existing approved trainee information form if the user is authorized.
     *
     * @param id          The ID of the trainee form to be updated.
     * @param username    The username of the student attempting to update the form.
     * @param updatedForm The new form data containing the updated values.
     * @return The updated ApprovedTraineeInformationForm object.
     * @throws RuntimeException If the form is not found or if the user is not authorized to update it.
     */
    @Transactional
    public ApprovedTraineeInformationForm updateApprovedTraineeInformationForm(Integer id, String username, ApprovedTraineeInformationForm updatedForm) {
        // Check if form exists
        ApprovedTraineeInformationForm form = approvedTraineeInformationFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Form not found! ID: " + id));

        // Check if the user is the owner
        if (!form.getFillUserName().getUserName().equals(username)) {
            throw new RuntimeException("Unauthorized action! User " + username + " is not the owner of this form.");
        }

        // Update form fields
        form.setPosition(updatedForm.getPosition());
        form.setType(updatedForm.getType());
        form.setCode(updatedForm.getCode());
        form.setSemester(updatedForm.getSemester());
        form.setSupervisorName(updatedForm.getSupervisorName());
        form.setSupervisorSurname(updatedForm.getSupervisorSurname());
        form.setHealthInsurance(updatedForm.getHealthInsurance());
        form.setInsuranceApproval(updatedForm.getInsuranceApproval());
        form.setInsuranceApprovalDate(updatedForm.getInsuranceApprovalDate());
        form.setStatus(updatedForm.getStatus());
        form.setInternshipStartDate(updatedForm.getInternshipStartDate());
        form.setInternshipEndDate(updatedForm.getInternshipEndDate());
        form.setCountry(updatedForm.getCountry());
        form.setCity(updatedForm.getCity());
        form.setDistrict(updatedForm.getDistrict());

        return approvedTraineeInformationFormRepository.save(form);
    }

    public List<ApprovedTraineeInformationFormDTO> getAllApprovedTraineeFormDTOs() {
        return approvedTraineeInformationFormRepository.findAllInternshipDTOs();
    }

    /**
     * Rejects an internship and notifies the student via email.
     *
     * @param internshipId The ID of the internship to be rejected.
     */
    @Transactional
    public void rejectInternship(Integer internshipId) {
        ApprovedTraineeInformationForm form = approvedTraineeInformationFormRepository
                .findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found with id: " + internshipId));

        form.setStatus("Rejected");
        approvedTraineeInformationFormRepository.save(form);

        // Send email to student after rejection
        User student = userRepository.findByUserName(form.getFillUserName().getUserName());
        if (student != null && student.getEmail() != null && !student.getEmail().isBlank()) {
            String subject = "Your Internship Has Been Rejected by the Company";
            String body = "Dear Student,\n\n" +
                    "We regret to inform you that your internship has been rejected by the company.\n\n" +
                    "Kind regards,\nInternship Management System";
            emailService.sendEmail(student.getEmail(), subject, body);
        }
    }


    /**
     * Returns Approved Trainee Forms of a student directly as DTOs
     * to reduce entity-to-DTO mapping cost and improve response time.
     *
     * @param username The username of the student
     * @return List of ApprovedTraineeInformationFormDTO
     */
    @Transactional
    public List<ApprovedTraineeInformationFormDTO> getApprovedTraineeFormDTOsOfStudent(String username) {
        return approvedTraineeInformationFormRepository.findAllInternshipDTOsByUsername(username);
    }

    /**
     * Returns only approved internships as DTOs for Student Affairs,using projection to reduce data load and improve speed.
     * @return List of ApprovedTraineeInformationFormDTO with only essential data.
     */
    public List<ApprovedTraineeInformationFormDTO> getApprovedInternshipDTOsForStudentAffairs() {
        return approvedTraineeInformationFormRepository.findApprovedInternshipsForStudentAffairs();
    }

}