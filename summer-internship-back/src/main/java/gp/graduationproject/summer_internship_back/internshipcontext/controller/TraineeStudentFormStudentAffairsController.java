package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InsuranceApprovalLog;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InsuranceApprovalLogRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for managing student affairs related to approved internships.
 */
@RestController
@RequestMapping("/api/studentAffairs")
public class TraineeStudentFormStudentAffairsController {

    private final ApprovedTraineeInformationFormService approvedTraineeInformationFormService;
    private final InsuranceApprovalLogRepository insuranceApprovalLogRepository;

    /**
     * Constructs an instance of the controller with required dependencies.
     *
     * @param approvedTraineeInformationFormService Service for handling approved trainee information forms.
     * @param insuranceApprovalLogRepository Repository for accessing insurance approval logs.
     */
    @Autowired
    public TraineeStudentFormStudentAffairsController(
            ApprovedTraineeInformationFormService approvedTraineeInformationFormService,
            InsuranceApprovalLogRepository insuranceApprovalLogRepository) {
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
        this.insuranceApprovalLogRepository = insuranceApprovalLogRepository;
    }

    /**
     * Retrieves a list of all approved internships.
     *
     * @return List of approved internships in DTO format.
     */
    @GetMapping("/approvedInternships")
    public ResponseEntity<List<ApprovedTraineeInformationFormDTO>> getAllApprovedInternships() {
        List<ApprovedTraineeInformationForm> internships = approvedTraineeInformationFormService.getApprovedTraineeInformationForms()
                .stream()
                .filter(form -> "Approved".equals(form.getStatus()))
                .toList();

        List<ApprovedTraineeInformationFormDTO> internshipDTOs = internships.stream()
                .map(form -> new ApprovedTraineeInformationFormDTO(
                        form.getId(),
                        form.getFillUserName().getUsers().getFirstName(),
                        form.getFillUserName().getUsers().getLastName(),
                        form.getFillUserName().getUserName(),
                        form.getDatetime(),
                        form.getPosition(),
                        form.getType(),
                        form.getCode(),
                        form.getSemester(),
                        form.getSupervisorName(),
                        form.getSupervisorSurname(),
                        form.getHealthInsurance(),
                        form.getInsuranceApproval(),
                        form.getInsuranceApprovalDate(),
                        form.getStatus(),
                        form.getCompanyBranch().getCompanyUserName().getUserName(),
                        form.getCompanyBranch().getBranchName(),
                        form.getCompanyBranch().getAddress(),
                        form.getCompanyBranch().getPhone(),
                        form.getCompanyBranch().getBranchEmail(),
                        form.getCompanyBranch().getCountry(),
                        form.getCompanyBranch().getCity(),
                        form.getCompanyBranch().getDistrict(),
                        form.getCoordinatorUserName() != null ? form.getCoordinatorUserName().getUserName() : "Unknown",
                        form.getEvaluatingFacultyMember() != null ? form.getEvaluatingFacultyMember() : "Unknown",
                        form.getInternshipStartDate(),
                        form.getInternshipEndDate(),
                        form.getEvaluateForms().stream()
                                .map(e -> new EvaluateFormDTO(e.getId(), e.getWorkingDay(), e.getPerformance(), e.getFeedback()))
                                .toList(),
                        form.getReports().stream()
                                .map(r -> new ReportDTO(r.getId(), r.getGrade(), r.getFeedback(), r.getStatus(), r.getCreatedAt()))
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(internshipDTOs);
    }

    /**
     * Approves insurance for a specific internship and logs the approval.
     *
     * @param internshipId ID of the internship to approve insurance for.
     * @param approvedBy The username of the student affairs officer who approved the insurance.
     * @return Confirmation message upon successful update.
     */
    @PostMapping("/approveInsurance")
    @Transactional
    public ResponseEntity<String> approveInsurance(@RequestParam Integer internshipId, @RequestParam String approvedBy) {
        approvedTraineeInformationFormService.approveInsurance(internshipId, approvedBy);
        return ResponseEntity.ok("Insurance approval updated successfully and logged.");
    }

    /**
     * Exports a list of today's approved internships to an Excel file.
     * Only internships approved on the current day will be included.
     *
     * @return Excel file containing today's approved internships.
     * @throws IOException If an error occurs during file creation.
     */
    @GetMapping("/exportApprovedInternships")
    public ResponseEntity<byte[]> exportApprovedInternshipsToExcel() throws IOException {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        // Retrieve insurance approval logs for today's approved internships
        List<InsuranceApprovalLog> approvedLogs = insuranceApprovalLogRepository
                .findByApprovalDateBetween(todayStart, todayEnd);

        // Create an Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Approved Internships");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Student Username", "Internship Start Date", "Internship End Date",
                "Company Address", "Health Insurance Approved", "Approved By"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Add data to Excel sheet
        int rowNum = 1;
        for (InsuranceApprovalLog log : approvedLogs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(log.getStudentUserName());
            row.createCell(1).setCellValue(log.getInternshipStartDate().toString());
            row.createCell(2).setCellValue(log.getInternshipEndDate().toString());
            row.createCell(3).setCellValue(log.getBranchAddress());
            row.createCell(4).setCellValue(log.getHealthInsurance() ? "Yes" : "No");
            row.createCell(5).setCellValue(log.getApprovedBy());
        }

        // Convert Excel file to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        byte[] excelBytes = outputStream.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=today_approved_internships.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelBytes);
    }
}