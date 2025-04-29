package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ReportEvaluation;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ReportEvaluationService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportEvaluationDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report-evaluations")
public class ReportEvaluationController {

    private final ReportEvaluationService reportEvaluationService;

    public ReportEvaluationController(ReportEvaluationService reportEvaluationService) {
        this.reportEvaluationService = reportEvaluationService;
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
}