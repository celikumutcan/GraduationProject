package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling report-related operations.
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * Constructor to inject ReportService.
     *
     * @param reportService the service responsible for report operations
     */
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Adds a new report.
     *
     * @param reportDTO the report data
     * @return the created report
     */
    @PostMapping
    public ResponseEntity<Report> addReport(@RequestBody ReportDTO reportDTO) {
        Report savedReport = reportService.addReport(reportDTO);
        return ResponseEntity.ok(savedReport);
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
     * Retrieves reports by trainee information form ID and status.
     *
     * @param traineeFormId the trainee information form ID
     * @param status the status filter
     * @return a list of reports
     */
    @GetMapping("/trainee/{traineeFormId}")
    public ResponseEntity<List<Report>> getReportsByTraineeFormIdAndStatus(
            @PathVariable Integer traineeFormId, @RequestParam String status) {
        List<Report> reports = reportService.getReportsByTraineeFormIdAndStatus(traineeFormId, status);
        return ResponseEntity.ok(reports);
    }
}