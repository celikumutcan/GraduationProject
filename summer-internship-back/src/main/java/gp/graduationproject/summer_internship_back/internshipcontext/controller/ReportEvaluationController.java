package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.InitialTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ReportEvaluation;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ReportRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.EmailService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportEvaluationService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.UserService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportEvaluationDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/report-evaluations")
public class ReportEvaluationController {

    private final ReportEvaluationService reportEvaluationService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;


    public ReportEvaluationController(ReportEvaluationService reportEvaluationService,EmailService emailService,UserRepository userRepository,ReportRepository reportRepository) {
        this.reportEvaluationService = reportEvaluationService;
        this.emailService = emailService;
        this.userRepository=userRepository;
        this.reportRepository=reportRepository;
    }

    @PostMapping
    public ResponseEntity<Void> createAllEvaluations(@RequestBody ReportEvaluationDTO dto) {
        reportEvaluationService.createAllEvaluations(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<List<ReportEvaluation>> getEvaluationsByReportId(@PathVariable Integer reportId) {
        List<ReportEvaluation> evaluations = reportEvaluationService.getEvaluationsByReportId(reportId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{id}/feedback")
    public ResponseEntity<String> getInstructorFeedback(@PathVariable("id") Integer reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Report not found with id " + reportId));

        String feedback = report.getInstructorFeedback();
        return ResponseEntity.ok(feedback != null ? feedback : "");
    }

    @GetMapping("/{reportId}/student-username")
    public ResponseEntity<String> getStudentUsername(@PathVariable Integer reportId) {
        String username = reportEvaluationService.getStudentUsernameByReportId(reportId);
        return ResponseEntity.ok(username);
    }


    @GetMapping("/{reportId}/export-csv")
    public ResponseEntity<byte[]> exportEvaluationsToCsv(@PathVariable Integer reportId) {
        byte[] csvContent = reportEvaluationService.exportEvaluationsToCsv(reportId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + reportId + "_evaluation.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
    }
    @GetMapping("/{reportId}/export-excel")
    public ResponseEntity<byte[]> exportEvaluationsToExcel(@PathVariable Integer reportId) {
        byte[] excelContent = reportEvaluationService.exportEvaluationsToExcel(reportId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_" + reportId + "_evaluation.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
    }
    private void sendEvaluationNotification(String studentUserName) {
        System.out.println("Evaluation notification method triggered for: " + studentUserName);

        User student = userRepository.findByUserName(studentUserName);
        if (student == null || student.getEmail() == null || student.getEmail().isBlank()) {
            System.out.println("Student not found or email missing.");
            return;
        }

        String to = student.getEmail();
        String subject = "Your Internship Report Has Been Evaluated";
        String body = "Dear " + student.getUserName() + ",\n\n"
                + "Your internship report has been evaluated by the instructor. "
                + "You can now review the results in the system.\n\n"
                + "Best regards,\nInternship Management System";

        emailService.sendEmail(to, subject, body);
    }

    @PutMapping("/{reportId}/reject")
    public ResponseEntity<Void> rejectReport(
            @PathVariable Integer reportId,
            @RequestParam String reason
    ) {
        reportEvaluationService.rejectReport(reportId, reason);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{reportId}/correction")
    public ResponseEntity<Void> CorrectionReport(
            @PathVariable Integer reportId,
            @RequestParam String reason
    ) {
        reportEvaluationService.CorrectionReport(reportId, reason);
        return ResponseEntity.ok().build();
    }


}