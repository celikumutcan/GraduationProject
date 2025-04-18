package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ReportRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.ByteArrayOutputStream;

/**
 * Service class for managing reports.
 */
@Service
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;
    private final ApprovedTraineeInformationFormRepository formRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    /**
     * Constructor to inject dependencies.
     *
     * @param reportRepository report repository
     * @param formRepository approved trainee information form repository
     * @param emailService email service for sending notifications
     */
    public ReportService(ReportRepository reportRepository, ApprovedTraineeInformationFormRepository formRepository,
                         EmailService emailService, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.formRepository = formRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    /**
     * Adds a new report after validating the student's ownership and form status.
     * If the student uploads a report for the second time, an email is sent to the assigned instructor.
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

        List<Report> existingReports = reportRepository.findAllByTraineeInformationForm_Id(reportDTO.getTraineeInformationFormId());

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

        Report savedReport = reportRepository.save(report);

        if (existingReports.size() == 1) {
            sendInstructorNotification(form, reportDTO.getUserName());
        }

        return savedReport;

    }

    /**
     * Sends an email notification to the assigned instructor when a student uploads a revised report.
     *
     * @param form The approved trainee information form.
     * @param studentUserName The username of the student uploading the revised report.
     */
    private void sendInstructorNotification(ApprovedTraineeInformationForm form, String studentUserName) {
        System.out.println("Instructor notification method triggered for: " + studentUserName);

        String instructorUserName = form.getEvaluatingFacultyMember();
        if (instructorUserName == null || instructorUserName.isBlank()) {
            System.out.println("Instructor username not found.");
            return;
        }

        User instructor = userRepository.findByUserName(instructorUserName);
        if (instructor == null || instructor.getEmail() == null || instructor.getEmail().isBlank()) {
            System.out.println("Instructor email not found.");
            return;
        }

        String instructorEmail = instructor.getEmail();

        String subject = "Student Revised Report Submission";
        String body = "Dear Instructor,\n\n" +
                "The student " + studentUserName + " has submitted a revised report.\n" +
                "Please review it at your earliest convenience.\n\n" +
                "Best regards,\nInternship Management System";

        emailService.sendEmail(instructorEmail, subject, body);
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
        // Find the report by ID
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + id));

        // Get the associated ApprovedTraineeInformationForm
        ApprovedTraineeInformationForm traineeForm = report.getTraineeInformationForm();

        if (traineeForm != null) {
            // Check if this is the only report linked to the trainee form
            if (traineeForm.getReports().size() == 1) {
                traineeForm.setStatus("Report Upload Waiting");
                formRepository.save(traineeForm);
            }
        }

        // Delete the report
        reportRepository.deleteById(id);

    }

    /**
     * Updates the status of a report and sends a feedback notification email to the student.
     *
     * @param reportId the ID of the report
     * @param status the new status (must be one of: APPROVED, REJECTED, RE-CHECK, Instructor Feedback Waiting)
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

        // Send feedback email to student
        ApprovedTraineeInformationForm form = report.getTraineeInformationForm();
        String studentEmail = form.getFillUserName().getUsers().getEmail();

        if (studentEmail != null && !studentEmail.isBlank()) {
            String subject = "Internship Report Feedback";
            String body = "Dear student,\n\n" +
                    "Your internship report has been reviewed. Please check the system for the feedback.\n\n" +
                    "Best regards,\nInternship Management System";
            emailService.sendEmail(studentEmail, subject, body);
        }
    }


    /**
     * Retrieves reports by trainee information from ID.
     *
     * @param traineeFormId the trainee information form ID
     * @return a list of reports
     */
    public List<Report> getReportsByTraineeFormId(Integer traineeFormId) {
        return reportRepository.findAllByTraineeInformationForm_Id(traineeFormId);
    }

    /**
     * Retrieves all reports linked to a specific Approved Trainee Information Form.
     *
     * @param traineeInformationFormId the ID of the trainee information form
     * @return a list of reports
     */
    public List<Report> getReportsByTraineeInformationFormId(Integer traineeInformationFormId) {
        return reportRepository.findAllByTraineeInformationForm_Id(traineeInformationFormId);
    }

    /**
     * Retrieves reports assigned to an instructor within a specified date range.
     *
     * @param instructorUserName The username of the instructor.
     * @param startDate The start date of the filter range.
     * @param endDate The end date of the filter range.
     * @return A list of reports matching the criteria.
     */
    public List<ReportDTO> getReportsByInstructorAndDateRange(String instructorUserName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Report> reports = reportRepository.findReportsByInstructorAndDateRange(instructorUserName, startDate, endDate);

        return reports.stream()
                .map(report -> new ReportDTO(report.getId(), report.getGrade(), report.getFeedback(), report.getStatus(), report.getCreatedAt()))
                .collect(Collectors.toList());
    }


    /**
     * Generates an Excel file containing reports within a specific date range.
     *
     * @param instructorUserName The username of the instructor.
     * @param startDate The start date for filtering reports.
     * @param endDate The end date for filtering reports.
     * @return Byte array of the generated Excel file.
     * @throws IOException if an error occurs while writing the file.
     */
    public byte[] generateExcelForInstructorReports(String instructorUserName, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
        List<Report> reports = reportRepository.findReportsByInstructorAndDateRange(instructorUserName, startDate, endDate);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Instructor Reports");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Username");
            headerRow.createCell(1).setCellValue("Grade");

            int rowNum = 1;
            for (Report report : reports) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(report.getTraineeInformationForm().getFillUserName().getUserName());
                row.createCell(1).setCellValue(report.getGrade());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}