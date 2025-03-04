package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    /**
     * Constructs an instance of the service with required dependencies.
     *
     * @param approvedTraineeInformationFormRepository Repository for accessing approved trainee forms.
     * @param studentRepository Repository for accessing student information.
     * @param academicStaffRepository Repository for accessing academic staff.
     */
    @Autowired
    public ApprovedTraineeInformationFormService(
            ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository,
            StudentRepository studentRepository,
            AcademicStaffRepository academicStaffRepository) {
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.studentRepository = studentRepository;
        this.academicStaffRepository = academicStaffRepository;
    }

    /**
     * Retrieves all approved trainee information forms for a specific student.
     *
     * @param userName The username of the student.
     * @return List of approved trainee forms belonging to the student.
     */


    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormOfStudent(String userName) {
        studentRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("Student not found"));
        return approvedTraineeInformationFormRepository.findAllByFillUserName_UserName(userName);
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
     * Retrieves all approved trainee information forms evaluated by a specific instructor.
     *
     * @param userName The username of the instructor.
     * @return List of trainee forms evaluated by the instructor.
     */
    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormOfInstructor(String userName) {
        return approvedTraineeInformationFormRepository.findAllByCoordinatorUserName_UserName(userName);
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
     * Approves the insurance for a specific internship.
     *
     * @param internshipId The ID of the internship.
     */
    @Transactional
    public void approveInsurance(@NonNull Integer internshipId) {
        ApprovedTraineeInformationForm internship = approvedTraineeInformationFormRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));
        internship.setInsuranceApproval(true);
        internship.setInsuranceApprovalDate(LocalDate.now());
        approvedTraineeInformationFormRepository.save(internship);
    }

    /**
     * Approves an internship, assigning a coordinator and an evaluating faculty member if necessary.
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
    }
    @Transactional
    public boolean updateFormStatus(Integer id, String status) {
        Optional<ApprovedTraineeInformationForm> formOptional = approvedTraineeInformationFormRepository.findById(id);

        if (formOptional.isPresent()) {
            ApprovedTraineeInformationForm form = formOptional.get();
            form.setStatus(status); // Statüyü güncelliyoruz
            approvedTraineeInformationFormRepository.save(form);
            return true;
        }
        return false;
    }


    @Transactional
    public ApprovedTraineeInformationForm save(ApprovedTraineeInformationForm form) {
        return approvedTraineeInformationFormRepository.save(form);
    }


}