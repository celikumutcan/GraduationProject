package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.InitialTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ApprovedTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.EvaluateFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InitialTraineeInformationFormDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportExportDTO;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Comparator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/traineeFormCoordinator")
public class TraineeStudentFormCoordinatorController {

    private final InitialTraineeInformationFormService initialTraineeInformationFormService;
    private final ApprovedTraineeInformationFormService approvedTraineeInformationFormService;
    private final ReportService reportService;
    private AcademicStaffRepository studentRepository;
    private final ApprovedTraineeInformationFormRepository formRepository;


    /**
     * Constructor to inject services.
     */
    public TraineeStudentFormCoordinatorController(
            InitialTraineeInformationFormService initialTraineeInformationFormService,
            ApprovedTraineeInformationFormService approvedTraineeInformationFormService,
            ReportService reportService,
            ApprovedTraineeInformationFormRepository formRepository) {
        this.initialTraineeInformationFormService = initialTraineeInformationFormService;
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
        this.reportService = reportService;
        this.formRepository = formRepository;
    }


    /**
     * Retrieves all trainee forms (initial and approved) for the coordinator view.
     */
    @GetMapping
    public ResponseEntity<List<Object>> getAllTraineeStudentForms() {
        List<InitialTraineeInformationFormDTO> initialFormDTOs =
                initialTraineeInformationFormService.getAllInitialTraineeFormDTOsForCoordinator();

        List<ApprovedTraineeInformationFormDTO> approvedFormDTOs =
                approvedTraineeInformationFormService.getAllApprovedTraineeFormDTOs();

        return ResponseEntity.ok(List.of(initialFormDTOs, approvedFormDTOs));
    }

    private InitialTraineeInformationFormDTO convertToInitialDTO(InitialTraineeInformationForm form) {
        return new InitialTraineeInformationFormDTO(
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
                form.getStatus(),
                form.getCompanyUserName(),
                form.getBranchName(),
                form.getCompanyBranchAddress(),
                form.getCompanyBranchPhone(),
                form.getCompanyBranchEmail(),
                form.getCountry(),
                form.getCity(),
                form.getDistrict(),
                form.getInternshipStartDate(),
                form.getInternshipEndDate(),
                form.getCoordinatorUserName() != null ? form.getCoordinatorUserName().getUserName() : "Unknown",
                form.getEvaluatingFacultyMember() != null ? form.getEvaluatingFacultyMember() : "Unknown"
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<InitialTraineeInformationFormDTO> getTraineeFormById(@PathVariable Integer id) {
        InitialTraineeInformationForm form = initialTraineeInformationFormService
                .getInitialTraineeInformationFormById(id)
                .orElseThrow(() -> new RuntimeException("Trainee Form not found with id: " + id));

        return ResponseEntity.ok(convertToInitialDTO(form));
    }


    /**

     Generates an Excel file containing students' grades for the Coordinator.
     This version uses DTO projection for performance optimization.
     If the student has no report or no grade, the grade cell remains empty.

     @param startDate The start date for filtering approved forms.
     @param endDate The end date for filtering approved forms.
     @return Byte array of the generated Excel file.
     @throws IOException if an error occurs while writing the file.
     */
    @GetMapping("/reports/download")
    public ResponseEntity<byte[]> generateExcelForCoordinatorReports(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) throws IOException {

        List<ReportExportDTO> forms = formRepository.findFormsForExport(startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student Grades");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("First Name");
            headerRow.createCell(1).setCellValue("Last Name");
            headerRow.createCell(2).setCellValue("Username");
            headerRow.createCell(3).setCellValue("Code");
            headerRow.createCell(4).setCellValue("Grade");

            // Data rows
            int rowNum = 1;
            for (ReportExportDTO form : forms) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(form.firstName());
                row.createCell(1).setCellValue(form.lastName());
                row.createCell(2).setCellValue(form.userName());
                row.createCell(3).setCellValue(form.code());
                row.createCell(4).setCellValue(form.grade() != null ? form.grade() : "");
            }

            // Write workbook to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Student_Grades.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        }
    }
}