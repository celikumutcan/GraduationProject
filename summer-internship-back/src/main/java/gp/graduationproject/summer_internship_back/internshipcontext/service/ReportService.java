package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ReportRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing reports.
 */
@Service
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;
    private final ApprovedTraineeInformationFormRepository formRepository;

    /**
     * Constructor to inject dependencies.
     *
     * @param reportRepository report repository
     * @param formRepository approved trainee information form repository
     */
    public ReportService(ReportRepository reportRepository, ApprovedTraineeInformationFormRepository formRepository) {
        this.reportRepository = reportRepository;
        this.formRepository = formRepository;
    }

    /**
     * Adds a new report after validating the student's ownership and form status.
     *
     * @param reportDTO the report data
     * @return the saved report
     * @throws RuntimeException if the student is not allowed to add a report
     */
    public Report addReport(ReportDTO reportDTO) {
        Optional<ApprovedTraineeInformationForm> formOpt = formRepository.findById(reportDTO.getTraineeInformationFormId());

        if (formOpt.isEmpty()) {
            throw new RuntimeException("Trainee information form not found");
        }

        ApprovedTraineeInformationForm form = formOpt.get();

        if (!form.getFillUserName().getUserName().equals(reportDTO.getUserName())) {
            throw new RuntimeException("You are not allowed to submit a report for this trainee form");
        }

        if (!"Approved".equals(form.getStatus())) {
            throw new RuntimeException("You can only upload a report if the form status is 'Approved'");
        }

        Report report = new Report();
        report.setTraineeInformationForm(form);
        report.setGrade(reportDTO.getGrade());
        report.setFeedback(reportDTO.getFeedback());
        report.setStatus("Instructor Feedback Waiting");

        MultipartFile file = reportDTO.getFile();
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Report file is required");
        }

        try {
            report.setFile(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error processing file", e);
        }

        return reportRepository.save(report);
    }

    /**
     * Retrieves all reports.
     *
     * @return a list of reports
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Retrieves a specific report by ID.
     *
     * @param id the ID of the report
     * @return the requested report
     * @throws RuntimeException if the report is not found
     */
    public Report getReportById(Integer id) {
        return reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
    }

    /**
     * Deletes a report by ID.
     *
     * @param id the ID of the report to delete
     */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

    /**
     * Updates the status of a report.
     *
     * @param reportId the ID of the report
     * @param status the new status
     * @throws RuntimeException if the report is not found
     * @throws IllegalArgumentException if the status value is invalid
     */
    public void updateReportStatus(Integer reportId, String status) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (!status.equals("APPROVED") && !status.equals("REJECTED") && !status.equals("RE-CHECK") && !status.equals("Instructor Feedback Waiting")) {
            throw new IllegalArgumentException("Invalid status value");
        }

        report.setStatus(status);
        reportRepository.save(report);
    }

    /**
     * Retrieves reports by trainee information form ID and status.
     *
     * @param traineeFormId the trainee information form ID
     * @param status the status filter
     * @return a list of reports
     */
    public List<Report> getReportsByTraineeFormIdAndStatus(Integer traineeFormId, String status) {
        return reportRepository.findAllByTraineeInformationForm_IdAndStatus(traineeFormId, status);
    }
}