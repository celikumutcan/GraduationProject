package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ApprovedTraineeInformationFormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
                .map(report -> new ReportDTO(report.getId(), report.getGrade(), report.getFeedback(), report.getStatus()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/reports/{reportId}/status")
    public ResponseEntity<String> updateReportStatus(@PathVariable Integer reportId, @RequestParam String status) {
        reportService.updateReportStatus(reportId, status);
        return ResponseEntity.ok("Report status updated to " + status);
    }
}