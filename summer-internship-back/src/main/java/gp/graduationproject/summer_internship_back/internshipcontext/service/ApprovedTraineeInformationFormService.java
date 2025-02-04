package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ApprovedTraineeInformationFormService {

    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    private final StudentRepository studentRepository;

    public ApprovedTraineeInformationFormService(ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository, StudentRepository studentRepository)
    {
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves all approved trainee information forms for a specific student.
     *
     * @param userName The username of the student.
     * @return List of approved trainee information forms for the student.
     */
    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormOfStudent(String userName)
    {
        studentRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("Student not found"));
        return approvedTraineeInformationFormRepository.findAllByFillUserName_UserName(userName);
    }

    /**
     * Retrieves all approved trainee information forms.
     *
     * @return List of all approved trainee information forms.
     */
    public List<ApprovedTraineeInformationForm> getApprovedTraineeInformationForms()
    {
        return approvedTraineeInformationFormRepository.findAll();
    }

    /**
     * Retrieves all approved trainee information forms evaluated by a specific instructor.
     *
     * @param userName The username of the instructor.
     * @return List of approved trainee information forms evaluated by the instructor.
     */
    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormOfInstructor(String userName)
    {
        return approvedTraineeInformationFormRepository.findAllByEvaluateUserName_UserName(userName);
    }

    /**
     * Retrieves all approved trainee information forms associated with a specific company branch.
     *
     * @param id The ID of the company branch.
     * @return List of approved trainee information forms for the company.
     */
    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormOfCompany(Integer id)
    {
        return approvedTraineeInformationFormRepository.findAllByCompanyBranch_Id(id);
    }

    /**
     * Approves the insurance for an internship.
     *
     * @param internshipId The ID of the internship.
     */
    @Transactional
    public void approveInsurance(Integer internshipId)
    {
        ApprovedTraineeInformationForm internship = approvedTraineeInformationFormRepository.findById(internshipId).orElseThrow(() -> new RuntimeException("Internship not found"));
        internship.setInsuranceApproval(true);
        internship.setInsuranceApprovalDate(Instant.now());
        approvedTraineeInformationFormRepository.save(internship);
    }

    /**
     * Approves an internship.
     *
     * @param internshipId The ID of the internship.
     */
    @Transactional
    public void approveInternship(Integer internshipId)
    {
        ApprovedTraineeInformationForm internship = approvedTraineeInformationFormRepository.findById(internshipId).orElseThrow(() -> new RuntimeException("Internship not found"));
        internship.setStatus("Approved");
        approvedTraineeInformationFormRepository.save(internship);
    }
}