package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.service.AcademicStaffService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.AcademicStaffDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller gives API to get academic staff list.
 */
@RestController
@RequestMapping("/api/academicStaff")
public class AcademicStaffController {

    private final AcademicStaffService academicStaffService;

    public AcademicStaffController(AcademicStaffService academicStaffService) {
        this.academicStaffService = academicStaffService;
    }

    /**
     * Get all academic staff as DTOs (username, first name, last name only).
     * @return list of AcademicStaffDTO
     */
    @GetMapping("/all")
    public List<AcademicStaffDTO> getAllAcademicStaff() {
        return academicStaffService.getAllAcademicStaffDTOs();
    }
}