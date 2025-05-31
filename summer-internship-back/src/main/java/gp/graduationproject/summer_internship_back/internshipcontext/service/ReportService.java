package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedTraineeInformationForm;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.User;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedTraineeInformationFormRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ReportRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.MinimalFormDataDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ReportDTO;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    private final Path studentReportStorageLocation = Paths.get("uploads/Student_Report").toAbsolutePath().normalize();


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
     * Optimized for performance using minimal data loading and count query.
     *
     * @param reportDTO the report data
     * @return the saved report
     * @throws RuntimeException if the student is not allowed to add a report
     */
    public Report addReport(ReportDTO reportDTO) {
        Integer formId = reportDTO.getTraineeInformationFormId();

        // Use minimal projection to avoid loading the full entity
        MinimalFormDataDTO formData = formRepository.findMinimalFormDataById(formId)
                .orElseThrow(() -> new RuntimeException("Trainee information form not found"));

        // Check if the report is submitted by the correct student
        if (!formData.getFillUserName().equals(reportDTO.getUserName())) {
            throw new RuntimeException("You are not allowed to submit a report for this trainee form");
        }

        // Allow only if the form is approved
        if (!"Approved".equals(formData.getStatus())) {
            throw new RuntimeException("You can only upload a report if the form status is 'Approved'");
        }

        // Check for existing reports
        int reportCount = reportRepository.countByTraineeInformationFormId(formId);
        if (reportCount >= 2) {
            throw new RuntimeException("Maximum 2 reports can be uploaded.");
        }

        MultipartFile file = reportDTO.getFile();
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Report file is required");
        }

        try {
            Files.createDirectories(studentReportStorageLocation);
            String fileName = reportDTO.getUserName() + "_report" + (reportCount + 1) + ".pdf";
            Path targetPath = studentReportStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Create a lightweight entity reference
            ApprovedTraineeInformationForm formRef = new ApprovedTraineeInformationForm();
            formRef.setId(formId);

            Report report = new Report();
            report.setTraineeInformationForm(formRef);
            report.setGrade(reportDTO.getGrade());
            report.setFeedback(reportDTO.getFeedback());
            report.setStatus("Instructor Feedback Waiting");
            report.setFile(file.getBytes());

            Report savedReport = reportRepository.save(report);

            if (reportCount == 1) {
                sendInstructorNotification(formData.getEvaluatingFacultyMember(), reportDTO.getUserName());
            }

            return savedReport;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save report file", e);
        }
    }


    /**
     * Sends an email notification to the instructor when a student submits a second report.
     *
     * @param instructorUserName The username of the instructor
     * @param studentUserName The username of the student who submitted the report
     */
    private void sendInstructorNotification(String instructorUserName, String studentUserName) {
        System.out.println("Instructor notification method triggered for: " + studentUserName);

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
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // Only fetch file, ignore other fields if needed
        if (report.getFile() == null) {
            throw new RuntimeException("File not found for this report.");
        }
        return report;
    }

    /**
     * Deletes a report by ID without modifying form status.
     *
     * @param id the ID of the report to delete
     */
    public void deleteReport(Integer id) {
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
     * Retrieves reports by trainee information form ID without including file data.
     *
     * @param traineeInformationFormId the trainee information form ID
     * @return a list of ReportDTOs
     */
    public List<ReportDTO> getReportsByTraineeInformationFormId(Integer traineeInformationFormId) {
        return reportRepository.findLightweightReportDTOsByFormId(traineeInformationFormId);
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


    public void uploadReportsFromFolder() {
        File reportDirectory = Paths.get("Student_Report").toFile();

        if (!reportDirectory.exists() || !reportDirectory.isDirectory()) {
            throw new RuntimeException("Student_Report klasörü bulunamadı.");
        }

        File[] files = reportDirectory.listFiles((dir, name) -> name.endsWith(".pdf"));
        if (files == null || files.length == 0) {
            throw new RuntimeException("Yüklenecek PDF bulunamadı.");
        }

        for (File file : files) {
            String fileName = file.getName(); // e.g., e245310_report1.pdf
            String[] parts = fileName.split("_");
            if (parts.length != 2 || !parts[1].startsWith("report") || !parts[1].endsWith(".pdf")) {
                System.out.println("Geçersiz dosya ismi formatı: " + fileName);
                continue;
            }

            String userName = parts[0];
            String reportNo = parts[1].replace(".pdf", ""); // e.g., report1

            List<ApprovedTraineeInformationForm> forms = formRepository.findAllByFillUserName_UserName(userName)
                    .stream()
                    .filter(form -> "Approved".equals(form.getStatus()))
                    .collect(Collectors.toList());

            if (forms.isEmpty()) {
                System.out.println("Approved form bulunamadı: " + userName);
                continue;
            }

            ApprovedTraineeInformationForm form = forms.get(0);

            List<Report> existingReports = reportRepository.findAllByTraineeInformationForm_Id(form.getId());
            if (existingReports.size() >= 2) {
                System.out.println("Zaten 2 rapor var, atlanıyor: " + userName);
                continue;
            }

            try {
                byte[] content = java.nio.file.Files.readAllBytes(file.toPath());
                Report report = new Report();
                report.setTraineeInformationForm(form);
                report.setStatus("Instructor Feedback Waiting");
                report.setFile(content);
                reportRepository.save(report);
                System.out.println(userName + " için " + reportNo + " yüklendi.");
            } catch (IOException e) {
                System.out.println("Dosya okunamadı: " + fileName);
            }
        }
    }
    public void uploadReportsFromFolderForStudent(String studentUserName) {
        String folderPath = "Student_Report";
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Student_Report klasörü bulunamadı.");
        }

        File[] studentFiles = folder.listFiles((dir, name) -> name.startsWith(studentUserName + "_") && name.endsWith(".pdf"));
        if (studentFiles == null || studentFiles.length == 0) {
            throw new RuntimeException("Öğrenciye ait rapor dosyası bulunamadı.");
        }

        List<ApprovedTraineeInformationForm> approvedForms = formRepository.findAllByFillUserName_UserName(studentUserName)
                .stream().filter(f -> "Approved".equals(f.getStatus())).toList();

        if (approvedForms.isEmpty()) {
            throw new RuntimeException("Approved form bulunamadı.");
        }

        ApprovedTraineeInformationForm form = approvedForms.get(0);
        List<Report> existingReports = reportRepository.findAllByTraineeInformationForm_Id(form.getId());
        if (existingReports.size() >= 2) {
            throw new RuntimeException("Maksimum 2 rapor yüklenebilir.");
        }

        int existingCount = existingReports.size();
        int uploaded = 0;

        for (int i = 0; i < studentFiles.length && existingCount + uploaded < 2; i++) {
            File reportFile = studentFiles[i];
            try {
                byte[] fileBytes = java.nio.file.Files.readAllBytes(reportFile.toPath());

                Report report = new Report();
                report.setTraineeInformationForm(form);
                report.setGrade(null);
                report.setFeedback(null);
                report.setStatus("Instructor Feedback Waiting");
                report.setFile(fileBytes);

                reportRepository.save(report);
                uploaded++;
            } catch (IOException e) {
                throw new RuntimeException("Dosya okunurken hata oluştu: " + reportFile.getName(), e);
            }
        }
    }
}