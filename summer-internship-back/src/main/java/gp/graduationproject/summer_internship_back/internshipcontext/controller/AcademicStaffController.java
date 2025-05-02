package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.service.AcademicStaffService;
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
     * Get all academic staff.
     * @return list of academic staff
     */
    @GetMapping("/all")
    public List<AcademicStaff> getAllAcademicStaff() {
        return academicStaffService.getAllAcademicStaff();
    }
}