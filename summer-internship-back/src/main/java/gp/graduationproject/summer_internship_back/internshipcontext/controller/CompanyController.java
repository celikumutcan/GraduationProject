package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.AnnouncementService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.AnnouncementDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {


    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyController(CompanyService companyService, CompanyRepository companyRepository) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companyDTOS = companyService.getAllCompanies()
                .stream()
                .map(company -> {
                    String name = company.getUsers().getFirstName();
                    return new CompanyDTO(
                            name
                    );
                })
                .toList();

        return ResponseEntity.ok(companyDTOS);
    }
}
