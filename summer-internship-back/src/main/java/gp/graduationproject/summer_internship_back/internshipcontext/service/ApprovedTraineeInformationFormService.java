package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InitialTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovedTraineeInformationFormService {
    @Autowired
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AcademicStaffRepository academicStaffRepository;

    public ApprovedTraineeInformationFormService(ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository) {
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
    }
    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormofStudent(String userName) {
        // Check if the student exists
        studentRepository.findByUserName(userName).orElseThrow(() -> new RuntimeException("Student not found"));

        // Fetch all forms for the given student
        return approvedTraineeInformationFormRepository.findAllByFillUserName_UserName(userName);
    }

    public List<ApprovedTraineeInformationForm> getApprovedTraineeInformationForms() {
        return approvedTraineeInformationFormRepository.findAll();
    }

    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormofInstructor(String userName) {

        return approvedTraineeInformationFormRepository.findAllByEvaluateUserName_UserName(userName);
    }

    @Transactional
    public List<ApprovedTraineeInformationForm> getAllApprovedTraineeInformationFormofCompany(Integer id) {

        return approvedTraineeInformationFormRepository.findAllByCompanyBranch_Id(id);
    }
}
