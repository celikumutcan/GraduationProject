package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.CompanyBranch;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.CompanyRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyBranchService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.CompanyService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.AnnouncementDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.CompanyBranchDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.CompanyDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InitialTraineeInformationFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/company-branch")
public class CompanyBranchController {


    private final CompanyBranchService companyBranchService;
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyBranchController(CompanyBranchService companyBranchService, CompanyRepository companyRepository) {
        this.companyBranchService = companyBranchService;
        this.companyRepository = companyRepository;
    }

    @PostMapping
    public ResponseEntity<List<CompanyBranchDTO>> getCompanyBranches(@RequestBody String username) {

        List<CompanyBranch> companyBranches = companyBranchService.getAllCompanyBranchesofCompany(username);

        List<CompanyBranchDTO> companyBranchDTOS = companyBranches.stream()
                .map(branch -> new CompanyBranchDTO(
                      branch.getBranchName()
                ))
                .toList();

        return ResponseEntity.ok(companyBranchDTOS);
    }
}
