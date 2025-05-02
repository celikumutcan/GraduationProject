package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service is for Academic Staff operations.
 * It gets data from AcademicStaffRepository.
 */
@Service
public class AcademicStaffService {

    private final AcademicStaffRepository academicStaffRepository;

    public AcademicStaffService(AcademicStaffRepository academicStaffRepository) {
        this.academicStaffRepository = academicStaffRepository;
    }

    /**
     * Get all academic staff from database.
     * @return list of academic staff
     */
    public List<AcademicStaff> getAllAcademicStaff() {
        return academicStaffRepository.findAll();
    }
}