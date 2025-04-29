package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling report-related operations.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository;
    /**
     * Constructor to inject ReportService.
     *
     * @param reportService the service responsible for report operations
     */
    public ReportController(ReportService reportService, ApprovedTraineeInformationFormRepository approvedTraineeInformationFormRepository) {
        this.reportService = reportService;
        this.approvedTraineeInformationFormRepository = approvedTraineeInformationFormRepository;
    }

    /**
     * Uploads a report file.
     *
     * @param traineeInformationFormId the ID of the trainee information form
     * @param userName the username of the student
     * @param grade the grade for the report
     * @param feedback feedback for the report
     * @param file the report file (PDF)
     * @return the created report
     */
    @PostMapping("/upload")
    public ResponseEntity<Report> uploadReport(
            @RequestParam Integer traineeInformationFormId,
            @RequestParam String userName,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String feedback,
            @RequestParam org.springframework.web.multipart.MultipartFile file) {

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTraineeInformationFormId(traineeInformationFormId);
        reportDTO.setUserName(userName);
        reportDTO.setGrade(grade);
        reportDTO.setFeedback(feedback);
        reportDTO.setStatus("Instructor Feedback Waiting");
        reportDTO.setFile(file);



        Report savedReport = reportService.addReport(reportDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }

    /**
     * Retrieves all reports.
     *
     * @return a list of reports
     */
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Retrieves a specific report by ID.
     *
     * @param id the ID of the report
     * @return the requested report
     */
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Integer id) {
        Report report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    /**
     * Deletes a report by ID.
     *
     * @param id the ID of the report to delete
     * @return a response indicating success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Integer id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();

    }

    /**
     * Updates the status of a report.
     *
     * @param id the ID of the report
     * @param status the new status
     * @return a response indicating success
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateReportStatus(@PathVariable Integer id, @RequestParam String status) {
        reportService.updateReportStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all reports linked to a specific Approved Trainee Information Form.
     *
     * @param traineeInformationFormId the ID of the trainee information form
     * @return a list of reports for the given trainee form
     */
    @GetMapping("/trainee/{traineeInformationFormId}/reports")
    public ResponseEntity<List<Report>> getReportsByTraineeInformationFormId(@PathVariable Integer traineeInformationFormId) {
        List<Report> reports = reportService.getReportsByTraineeInformationFormId(traineeInformationFormId);
        return ResponseEntity.ok(reports);
    }




    /**
     * Downloads a report file.
     *
     * @param id the ID of the report
     * @return the report file as a downloadable response
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Integer id) {
        Report report = reportService.getReportById(id);
        byte[] fileContent = report.getFile();

        if (fileContent == null || fileContent.length == 0) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + id + ".pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    @PostMapping("/upload-from-folder")
    public ResponseEntity<String> uploadReportsFromFolder() {
        reportService.uploadReportsFromFolder();
        return ResponseEntity.ok("📥 Klasörden raporlar başarıyla yüklendi.");
    }
}