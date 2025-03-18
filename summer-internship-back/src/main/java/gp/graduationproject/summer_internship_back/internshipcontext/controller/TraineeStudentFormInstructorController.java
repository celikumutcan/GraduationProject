package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import java.io.IOException;

@RestController
@RequestMapping("/api/traineeFormInstructor")
public class TraineeStudentFormInstructorController {

    private final ApprovedTraineeInformationFormService approvedTraineeInformationFormService;
    private final ReportService reportService;

    public TraineeStudentFormInstructorController(ApprovedTraineeInformationFormService approvedTraineeInformationFormService, ReportService reportService) {
        this.approvedTraineeInformationFormService = approvedTraineeInformationFormService;
        this.reportService = reportService;
    }

    @GetMapping("/reports/{traineeFormId}")
    public ResponseEntity<List<ReportDTO>> getReportsForTrainee(@PathVariable Integer traineeFormId) {
        List<ReportDTO> reports = reportService.getAllReports().stream()
                .map(report -> new ReportDTO(report.getId(), report.getGrade(), report.getFeedback(), report.getStatus(), report.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/reports/{reportId}/status")
    public ResponseEntity<String> updateReportStatus(@PathVariable Integer reportId, @RequestParam String status) {
        reportService.updateReportStatus(reportId, status);
        return ResponseEntity.ok("Report status updated to " + status);
    }

    /**
     * Retrieves reports assigned to an instructor within a specified date range.
     *
     * @param instructorUserName The username of the instructor.
     * @param startDate The start date for filtering reports.
     * @param endDate The end date for filtering reports.
     * @return A list of reports matching the criteria.
     */
    @GetMapping("/reports/filter")
    public ResponseEntity<List<ReportDTO>> getFilteredReports(
            @RequestParam String instructorUserName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<ReportDTO> reports = reportService.getReportsByInstructorAndDateRange(instructorUserName, startDate, endDate);

        return ResponseEntity.status(HttpStatus.OK).body(reports);
    }

    /**
     * Generates and downloads an Excel file for reports assigned to an instructor within a specific date range.
     *
     * @param instructorUserName The username of the instructor.
     * @param startDate The start date for filtering reports.
     * @param endDate The end date for filtering reports.
     * @return The Excel file as a downloadable response.
     */
    @GetMapping("/reports/download")
    public ResponseEntity<byte[]> downloadReportsAsExcel(
            @RequestParam String instructorUserName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        try {
            byte[] excelFile = reportService.generateExcelForInstructorReports(instructorUserName, startDate, endDate);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Instructor_Reports.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ResponseEntity.status(HttpStatus.OK)
                    .headers(headers)
                    .body(excelFile);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}