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


    @PutMapping("/{id}/updateStatus")
    public ResponseEntity<String> updateTraineeFormStatus(@PathVariable Integer id, @RequestParam String status) {
        boolean updated = initialTraineeInformationFormService.updateInitialFormStatus(id, status);

        if (updated) {
            return ResponseEntity.ok("Form status updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Form not found or update failed.");
        }
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

    private ApprovedTraineeInformationFormDTO convertToApprovedDTO(ApprovedTraineeInformationForm form) {
        return new ApprovedTraineeInformationFormDTO(
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
                (form.getCoordinatorUserName() != null) ? form.getCoordinatorUserName().getUserName() : "Not Assigned",
                form.getEvaluatingFacultyMember(),
                form.getInternshipStartDate(),
                form.getInternshipEndDate(),
                form.getEvaluateForms().stream()
                        .map(e -> new EvaluateFormDTO(
                                e.getId(),
                                e.getAttendance(),
                                e.getDiligenceAndEnthusiasm(),
                                e.getContributionToWorkEnvironment(),
                                e.getOverallPerformance(),
                                e.getComments()
                        ))
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<InitialTraineeInformationFormDTO> getTraineeFormById(@PathVariable Integer id) {
        InitialTraineeInformationForm form = initialTraineeInformationFormService
                .getInitialTraineeInformationFormById(id)
                .orElseThrow(() -> new RuntimeException("Trainee Form not found with id: " + id));

        return ResponseEntity.ok(convertToInitialDTO(form));
    }

    @PostMapping("/rejectInternship")
    @Transactional
    public ResponseEntity<String> rejectInternship(@RequestParam Integer internshipId) {
        boolean updated = approvedTraineeInformationFormService.updateFormStatus(internshipId, "Rejected");
        if (updated) {
            return ResponseEntity.ok("Internship rejected successfully.");
        } else {
            return ResponseEntity.status(404).body("Internship not found.");
        }
    }

    /**
     * Generates an Excel file containing students' grades for Coordinator.
     * If the student has no report or no grade, the grade cell remains empty.
     *
     * @param startDate The start date for filtering approved forms.
     * @param endDate The end date for filtering approved forms.
     * @return Byte array of the generated Excel file.
     * @throws IOException if an error occurs while writing the file.
     */
    @GetMapping("/reports/download")
    public ResponseEntity<byte[]> generateExcelForCoordinatorReports(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) throws IOException {

        List<ApprovedTraineeInformationForm> forms = formRepository.findAllWithReportsByDatetimeBetween(startDate, endDate);
        forms.sort(Comparator.comparing(ApprovedTraineeInformationForm::getCode));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Student Grades");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("First Name");
            headerRow.createCell(1).setCellValue("Last Name");
            headerRow.createCell(2).setCellValue("Username");
            headerRow.createCell(3).setCellValue("Code");
            headerRow.createCell(4).setCellValue("Grade");

            int rowNum = 1;
            for (ApprovedTraineeInformationForm form : forms) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(form.getFillUserName().getUsers().getFirstName());
                row.createCell(1).setCellValue(form.getFillUserName().getUsers().getLastName());
                row.createCell(2).setCellValue(form.getFillUserName().getUserName());
                row.createCell(3).setCellValue(form.getCode());

                String grade = null;
                if (form.getReports() != null) {
                    for (Report report : form.getReports()) {
                        if (report.getGrade() != null) {
                            grade = report.getGrade();
                            break;
                        }
                    }
                }
                row.createCell(4).setCellValue(grade != null ? grade : "");
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Student_Grades.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        }
    }
}