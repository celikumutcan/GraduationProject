package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportEvaluationDTO;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ReportEvaluation;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ReportRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ReportEvaluationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportEvaluationService {
    private final ReportEvaluationRepository evaluationRepository;
    private final ReportRepository reportRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public ReportEvaluationService(ReportEvaluationRepository evaluationRepository, ReportRepository reportRepository, EmailService emailService,UserRepository userRepository) {
        this.evaluationRepository = evaluationRepository;
        this.reportRepository = reportRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public void createAllEvaluations(ReportEvaluationDTO dto) {
        Report report = reportRepository.findById(dto.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));

        saveEval(report, "Company Eval & Description", 5, dto.getCompanyEvalGrade(), dto.getCompanyEvalComment());
        saveEval(report, "Report Structure", 10, dto.getReportStructureGrade(), dto.getReportStructureComment());
        saveEval(report, "Abstract", 5, dto.getAbstractGrade(), dto.getAbstractComment());
        saveEval(report, "Problem Statement", 5, dto.getProblemStatementGrade(), dto.getProblemStatementComment());
        saveEval(report, "Introduction", 5, dto.getIntroductionGrade(), dto.getIntroductionComment());
        saveEval(report, "Theory", 10, dto.getTheoryGrade(), dto.getTheoryComment());
        saveEval(report, "Analysis", 10, dto.getAnalysisGrade(), dto.getAnalysisComment());
        saveEval(report, "Modelling", 15, dto.getModellingGrade(), dto.getModellingComment());
        saveEval(report, "Programming", 20, dto.getProgrammingGrade(), dto.getProgrammingComment());
        saveEval(report, "Testing", 10, dto.getTestingGrade(), dto.getTestingComment());
        saveEval(report, "Conclusion", 5, dto.getConclusionGrade(), dto.getConclusionComment());

        int totalScore = 0;
        totalScore += dto.getCompanyEvalGrade() != null ? dto.getCompanyEvalGrade() : 0;
        totalScore += dto.getReportStructureGrade() != null ? dto.getReportStructureGrade() : 0;
        totalScore += dto.getAbstractGrade() != null ? dto.getAbstractGrade() : 0;
        totalScore += dto.getProblemStatementGrade() != null ? dto.getProblemStatementGrade() : 0;
        totalScore += dto.getIntroductionGrade() != null ? dto.getIntroductionGrade() : 0;
        totalScore += dto.getTheoryGrade() != null ? dto.getTheoryGrade() : 0;
        totalScore += dto.getAnalysisGrade() != null ? dto.getAnalysisGrade() : 0;
        totalScore += dto.getModellingGrade() != null ? dto.getModellingGrade() : 0;
        totalScore += dto.getProgrammingGrade() != null ? dto.getProgrammingGrade() : 0;
        totalScore += dto.getTestingGrade() != null ? dto.getTestingGrade() : 0;
        totalScore += dto.getConclusionGrade() != null ? dto.getConclusionGrade() : 0;

        if (totalScore > 60) {
            report.setStatus("Graded");
            report.setGrade("S");
            reportRepository.save(report);
        }
        if (totalScore < 60) {
            report.setStatus("Graded");
            report.setGrade("U");
            reportRepository.save(report);
        }

        User student = userRepository.findByUserName(dto.getStudentUserName());
        if (student != null && student.getEmail() != null && !student.getEmail().isBlank()) {
            String subject = "Your Internship Report has been Evaluated!";
            String body = "Dear " + student.getUserName() + ",\n\n" +
                    "Your internship report has been successfully evaluated.\n\n" +
                    "Best regards,\nInternship Management System";

            emailService.sendEmail(student.getEmail(), subject, body);
        }
    }

    private void saveEval(Report report, String item, int weight, Double grade, String comment) {
        ReportEvaluation eval = evaluationRepository
                .findByReportAndItemName(report, item)
                .orElse(new ReportEvaluation());

        eval.setReport(report);
        eval.setItemName(item);
        eval.setWeight((double) weight);
        eval.setScore(grade);
        eval.setComment(comment);
        evaluationRepository.save(eval);
    }



    public List<ReportEvaluation> getEvaluationsByReportId(Integer reportId) {
        return evaluationRepository.findAllByReportId(reportId);
    }

    public byte[] exportEvaluationsToCsv(Integer reportId) {
        List<String> fixedItems = List.of(
                "Company Eval & Description", "Report Structure", "Abstract", "Problem Statement",
                "Introduction", "Theory", "Analysis", "Modelling", "Programming", "Testing", "Conclusion"
        );

        Map<String, Integer> weightMap = new HashMap<>();
        weightMap.put("Company Eval & Description", 5);
        weightMap.put("Report Structure", 10);
        weightMap.put("Abstract", 5);
        weightMap.put("Problem Statement", 5);
        weightMap.put("Introduction", 5);
        weightMap.put("Theory", 10);
        weightMap.put("Analysis", 10);
        weightMap.put("Modelling", 15);
        weightMap.put("Programming", 20);
        weightMap.put("Testing", 10);
        weightMap.put("Conclusion", 5);

        List<ReportEvaluation> evaluations = evaluationRepository.findAllByReportId(reportId);
        Map<String, ReportEvaluation> evaluationMap = evaluations.stream()
                .collect(Collectors.toMap(ReportEvaluation::getItemName, e -> e, (e1, e2) -> e1));

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Item,Weight,Grade,Comment\n");

        int totalScore = 0;

        for (String item : fixedItems) {
            int weight = weightMap.get(item);
            ReportEvaluation eval = evaluationMap.get(item);

            String gradeStr = "";
            String comment = "";

            if (eval != null && eval.getScore() != null) {
                gradeStr = String.valueOf(eval.getScore());
                totalScore += eval.getScore();
            }

            if (eval != null && eval.getComment() != null) {
                comment = eval.getComment();
            }

            csvBuilder.append(String.format("%s,%d,%s,%s\n", item, weight, gradeStr, comment));
        }

        csvBuilder.append("\nTotal Score: ").append(totalScore).append("\n");
        csvBuilder.append("Result: ").append(totalScore > 60 ? "PASS " : "FAIL ").append("\n");

        return csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
    public byte[] exportEvaluationsToExcel(Integer reportId) {
        List<String> fixedItems = List.of(
                "Company Eval & Description", "Report Structure", "Abstract", "Problem Statement",
                "Introduction", "Theory", "Analysis", "Modelling", "Programming", "Testing", "Conclusion"
        );
        Map<String, Integer> weightMap = new HashMap<>();
        weightMap.put("Company Eval & Description", 5);
        weightMap.put("Report Structure", 10);
        weightMap.put("Abstract", 5);
        weightMap.put("Problem Statement", 5);
        weightMap.put("Introduction", 5);
        weightMap.put("Theory", 10);
        weightMap.put("Analysis", 10);
        weightMap.put("Modelling", 15);
        weightMap.put("Programming", 20);
        weightMap.put("Testing", 10);
        weightMap.put("Conclusion", 5);


        List<ReportEvaluation> evaluations = evaluationRepository.findAllByReportId(reportId);
        Map<String, ReportEvaluation> evaluationMap = evaluations.stream()
                .collect(Collectors.toMap(ReportEvaluation::getItemName, e -> e, (e1, e2) -> e1));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report Evaluation");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Item");
            header.createCell(1).setCellValue("Weight");
            header.createCell(2).setCellValue("Grade");
            header.createCell(3).setCellValue("Comment");

            int rowIdx = 1;
            int totalScore = 0;

            for (String item : fixedItems) {
                ReportEvaluation eval = evaluationMap.get(item);
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item);
                row.createCell(1).setCellValue(weightMap.get(item));
                row.createCell(2).setCellValue(eval != null && eval.getScore() != null ? eval.getScore() : 0);
                row.createCell(3).setCellValue(eval != null && eval.getComment() != null ? eval.getComment() : "");

                if (eval != null && eval.getScore() != null) {
                    totalScore += eval.getScore();
                }
            }

            Row totalRow = sheet.createRow(rowIdx++);
            totalRow.createCell(0).setCellValue("Total Score");
            totalRow.createCell(1).setCellValue(totalScore);

            Row resultRow = sheet.createRow(rowIdx);
            resultRow.createCell(0).setCellValue("Result");
            resultRow.createCell(1).setCellValue(totalScore > 60 ? "PASS " : "FAIL ");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }

    public void rejectReport(Integer reportId, String reason) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("Rejected");
        reportRepository.save(report);

        User student = report.getTraineeInformationForm().getFillUserName().getUsers();
        if (student != null && student.getEmail() != null && !student.getEmail().isBlank()) {
            String subject = "Your Internship Report Has Been Rejected";
            String body = "Dear " + student.getUserName() + ",\n\n"
                    + "Your internship report has been rejected.\n"
                    + "Reason: " + reason + "\n\n"
                    + "Please contact your instructor for further details.\n\n"
                    + "Best regards,\nInternship Management System";

            emailService.sendEmail(student.getEmail(), subject, body);
        }
    }

    public void CorrectionReport(Integer reportId, String reason) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus("Instructor Feedback Waiting");
        reportRepository.save(report);

        User student = report.getTraineeInformationForm().getFillUserName().getUsers();
        if (student != null && student.getEmail() != null && !student.getEmail().isBlank()) {
            String subject = "Your Internship Report Has need to Correction";
            String body = "Dear " + student.getUserName() + ",\n\n"
                    + "Your internship report has been need to be Correction.\n"
                    + "Reason: " + reason + "\n\n"
                    + "Please contact your instructor for further details.\n\n"
                    + "Best regards,\nInternship Management System";

            emailService.sendEmail(student.getEmail(), subject, body);
        }
    }

}
