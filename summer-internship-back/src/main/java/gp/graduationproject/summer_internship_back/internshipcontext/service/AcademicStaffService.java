package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.AcademicStaffDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    /**
     * Get all academic staff as DTOs (username, first name, last name only).
     * @return list of AcademicStaffDTO
     */
    public List<AcademicStaffDTO> getAllAcademicStaffDTOs() {
        List<AcademicStaff> staffList = academicStaffRepository.findAll();
        List<AcademicStaffDTO> dtoList = new ArrayList<>(staffList.size());

        for (AcademicStaff staff : staffList) {
            AcademicStaffDTO dto = new AcademicStaffDTO(
                    staff.getUserName(),
                    staff.getUsers().getFirstName(),
                    staff.getUsers().getLastName()
            );
            dtoList.add(dto);
        }

        return dtoList;
    }
}