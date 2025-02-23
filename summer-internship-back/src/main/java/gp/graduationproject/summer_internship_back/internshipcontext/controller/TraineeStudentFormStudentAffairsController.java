package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for handling student affairs related to approved internships.
 */
@RestController
@RequestMapping("/api/studentAffairs")
public class TraineeStudentFormStudentAffairsController {

    @Autowired
    private ApprovedTraineeInformationFormService approvedTraineeInformationFormService;

    /**
     * Retrieves a list of all approved internships.
     *
     * @return List of approved internships in DTO format.
     */
    @GetMapping("/approvedInternships")
    public ResponseEntity<List<ApprovedTraineeInformationFormDTO>> getAllApprovedInternships() {
        List<ApprovedTraineeInformationForm> internships = approvedTraineeInformationFormService.getApprovedTraineeInformationForms();

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
                                .map(r -> new ReportDTO(r.getId(), r.getGrade(), r.getFeedback()))
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(internshipDTOs);
    }


    /**
     * Approves insurance for a specific internship.
     *
     * @param internshipId ID of the internship to approve insurance for.
     * @return Confirmation message upon successful update.
     */
    @PostMapping("/approveInsurance")
    @Transactional
    public ResponseEntity<String> approveInsurance(@RequestParam Integer internshipId) {
        approvedTraineeInformationFormService.approveInsurance(internshipId);
        return ResponseEntity.ok("Insurance approval updated successfully.");
    }

    /**
     * Exports a list of approved internships to an Excel file, optionally filtered by date.
     *
     * @param date Optional date parameter (format: YYYY-MM-DD) to filter internships approved on that day.
     * @return Excel file containing approved internships.
     * @throws IOException If an error occurs during file creation.
     */
    @GetMapping("/exportApprovedInternships")
    public ResponseEntity<byte[]> exportApprovedInternshipsToExcel(@RequestParam(required = false) String date) throws IOException {
        List<ApprovedTraineeInformationForm> approvedInternships;

        // If a date is provided, filter by approval date
        if (date != null && !date.isEmpty()) {
            LocalDate filterDate = LocalDate.parse(date);
            approvedInternships = approvedTraineeInformationFormService.getApprovedTraineeInformationForms()
                    .stream()
                    .filter(form -> form.getInsuranceApprovalDate() != null &&
                            form.getInsuranceApprovalDate().toString().startsWith(filterDate.toString()))
                    .collect(Collectors.toList());
        } else {
            // Otherwise, fetch all approved internships
            approvedInternships = approvedTraineeInformationFormService.getApprovedTraineeInformationForms();
        }

        // Create an Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Approved Internships");

        // Define column headers
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Student Number", "Internship Start Date", "Internship End Date", "Company Address", "Health Insurance Approved"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Populate the sheet with internship data
        int rowNum = 1;
        for (ApprovedTraineeInformationForm form : approvedInternships) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(form.getFillUserName().getUserName());
            row.createCell(1).setCellValue(form.getInternshipStartDate().toString());
            row.createCell(2).setCellValue(form.getInternshipEndDate().toString());
            row.createCell(3).setCellValue(form.getCompanyBranch().getAddress());
            row.createCell(4).setCellValue(form.getInsuranceApproval() ? "Yes" : "No");
        }

        // Convert the workbook to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        byte[] excelBytes = outputStream.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=approved_internships.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelBytes);
    }
}