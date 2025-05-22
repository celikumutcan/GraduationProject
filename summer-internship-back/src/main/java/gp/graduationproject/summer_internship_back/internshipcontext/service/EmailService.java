package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.*;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.UserRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.InternshipOfferCreateDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.MinimalInternshipDTO;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.OfferMailInfoDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class for sending emails, including support for attachments
 * and company branch welcome emails with password reset links.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;


    /**
     * Constructor for EmailService.
     */
    public EmailService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }


    /**
     * Sends an email with optional file attachment.
     *
     * @param recipients List of recipient email addresses.
     * @param subject Email subject.
     * @param body Email content (supports HTML).
     * @param file Optional file attachment.
     */
    public void sendEmailWithAttachment(List<String> recipients, String subject, String body, MultipartFile file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipients.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, true); // Enables HTML format

            // Attach file if provided
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + recipients);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Sends a welcome email to a company branch with username and plain password.
     *
     * @param recipientEmail Email address of the company branch
     * @param userName The generated username
     * @param plainPassword The generated plain password
     */
    public void sendCompanyBranchWelcomeEmail(String recipientEmail, String userName, String plainPassword) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            System.err.println("Error: recipient email is null or empty.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("Welcome to the Internship System");

            String emailContent = String.format("""
            <p>Dear Company Branch Representative,</p>
            <p>Your account has been created in the Internship Management System.</p>
            <p><b>Username:</b> %s</p>
            <p><b>Password:</b> %s</p>
            <p>Please change your password after your first login.</p>
            <p>Best regards,<br>Internship Management Team</p>
        """, userName, plainPassword);

            helper.setText(emailContent, true);
            mailSender.send(message);

            System.out.println("✅ Mail sent to: " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email to: " + recipientEmail);
            e.printStackTrace();
        }
    }


    /**
     * Sends a simple email to a single recipient.
     *
     * @param recipient The recipient email address.
     * @param subject Email subject.
     * @param body Email content.
     */
    @Async
    public void sendEmail(String recipient, String subject, String body) {
        System.out.println("[THREAD] Sending email on thread: " + Thread.currentThread().getName());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + recipient);

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Sends a password reset email to the company branch with a new password.
     *
     * @param recipientEmail The email address of the company branch
     * @param userName The username of the branch
     * @param plainPassword The new generated password
     */
    public void sendCompanyBranchResetPasswordEmail(String recipientEmail, String userName, String plainPassword) {
        if (recipientEmail == null || recipientEmail.isBlank()) {
            System.err.println("Invalid recipient email.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("Your New Password – Internship System");

            String emailContent = String.format("""
            <p>Dear Company Branch,</p>
            <p>You requested a new password for your Internship System account.</p>
            <p><b>Username:</b> %s</p>
            <p><b>New Password:</b> %s</p>
            <p>You can now log in using this password. Please change it after logging in.</p>
            <p>Best regards,<br>Internship Management Team</p>
        """, userName, plainPassword);

            helper.setText(emailContent, true);
            mailSender.send(message);

            System.out.println("✅ Reset password email sent to: " + recipientEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send reset email: " + recipientEmail);
            e.printStackTrace();
        }
    }


    /**
     * Sends email notifications asynchronously to both the company and the student
     * when an internship application is submitted.
     *
     * @param studentUserName the username of the student
     * @param internshipID the ID of the approved internship form
     * @param userRepository the repository to access user data
     * @param approvedFormService the service to get internship form data
     */
    @Async
    public void sendApprovalNotificationAsync(String studentUserName, Integer internshipID,
                                              UserRepository userRepository,
                                              ApprovedTraineeInformationFormService approvedFormService) {
        System.out.println("Approval notification triggered for: " + studentUserName);

        User student = userRepository.findByUserName(studentUserName);
        if (student == null || student.getEmail() == null || student.getEmail().isBlank()) {
            System.out.println("Student Not Found!");
            return;
        }
        String studentEmail = student.getEmail();

        Optional<MinimalInternshipDTO> minimalOpt = approvedFormService.getMinimalApprovedTraineeInformationFormById(internshipID);
        if (minimalOpt.isEmpty()) {
            System.out.println("Approved Internship Form Not Found!");
            return;
        }

        MinimalInternshipDTO minimal = minimalOpt.get();
        CompanyBranch companyBranch = minimal.getCompanyBranch();
        if (companyBranch == null || companyBranch.getBranchEmail() == null) {
            System.out.println("Company Branch Not Found!");
            return;
        }

        Optional<User> companyRepresentativeOpt = userRepository.findByEmailAndUserType(companyBranch.getBranchEmail(), "company_branch");
        if (companyRepresentativeOpt.isEmpty()) {
            System.out.println("Company Representative Not Found for email: " + companyBranch.getBranchEmail());
            return;
        }

        User companyRepresentative = companyRepresentativeOpt.get();
        String companyEmail = companyRepresentative.getEmail();
        String companyName = companyBranch.getBranchName();

        String companySubject = "New Internship Application Requires Approval";
        String companyBody = "Dear " + companyName + ",\n\n" +
                "A new internship application has been received and is waiting for your approval.\n\n" +
                "Student Name: " + student.getUserName() + "\n" +
                "Position: " + minimal.getPosition() + "\n\n" +
                "Please review it at your earliest convenience.\n\n" +
                "Best regards,\nInternship Management System";

        sendEmail(companyEmail, companySubject, companyBody);

        String studentSubject = "Your Internship Application Sent to Company";
        String studentBody = "Dear " + student.getUserName() + ",\n\n" +
                "Your internship form application has been sent to the company. Please wait for the company’s approval.\n" +
                "It is now waiting for company approval.\n\n" +
                "Best regards,\nInternship Management System";

        sendEmail(studentEmail, studentSubject, studentBody);
    }

    /**
     * Sends one email to all students about a new internship offer.
     *
     * @param studentEmails list of student email addresses
     * @param offer         the internship offer
     */
    @Async
    public void sendNewOfferNotificationToAllStudents(List<String> studentEmails, InternshipOffer offer) {
        if (studentEmails == null || studentEmails.isEmpty()) {
            System.out.println("No students to notify.");
            return;
        }

        String branchName = offer.getCompanyBranch() != null ? offer.getCompanyBranch().getBranchName() : "Unknown Branch";
        String companyUserName = offer.getCompanyBranch() != null && offer.getCompanyBranch().getCompanyUserName() != null
                ? offer.getCompanyBranch().getCompanyUserName().getUserName()
                : "Unknown Company";

        String subject = "New Internship Offer from " + companyUserName;

        String body = "Dear Students,\n\n"
                + companyUserName + " - " + branchName + " has posted a new internship offer.\n\n"
                + "• Position: " + offer.getPosition() + "\n"
                + "• Department: " + offer.getDepartment() + "\n"
                + "• Dates: " + offer.getStartDate() + " to " + offer.getEndDate() + "\n"
                + "• Details: " + offer.getDetails() + "\n\n"
                + "You can now apply from the internship system.\n\n"
                + "Best regards,\n"
                + "Internship Management System";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(studentEmails.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(body, false);

            mailSender.send(message);
            System.out.println("✅ Offer email sent to all students.");
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send offer email to students.");
            e.printStackTrace();
        }
    }

    @Async
    public void sendApplicationNotificationToCompanyBranchSimple(String studentUsername, String position, String branchEmail) {
        String subject = "New Internship Application";
        String body = "Dear Company,\n\n" +
                "A new internship application has been received.\n\n" +
                "Student: " + studentUsername + "\n" +
                "Position: " + position + "\n\n" +
                "Please review the application.\n\n" +
                "Best regards,\nInternship Management System";

        sendEmail(branchEmail, subject, body);
    }


    /**
     * Sends notification email to student based on application status.
     *
     * @param application the internship application
     * @param status      new status (Approved or Rejected)
     */
    /**
     * Sends notification email to student based on application status.
     *
     * @param studentEmail student's email address
     * @param studentUsername student's username
     * @param position internship position name
     * @param status new status (Approved or Rejected)
     */
    @Async
    public void sendApplicationStatusEmail(String studentEmail, String studentUsername, String position, String status) {

        String subject;
        String body;

        if ("Approved".equalsIgnoreCase(status)) {
            subject = "Your Internship Application Has Been Approved";
            body = String.format("""
                Dear %s,

                Congratulations! Your application for the internship position "%s" has been approved.

                Best regards,
                Internship Management System
                """, studentUsername, position);
        } else if ("Rejected".equalsIgnoreCase(status)) {
            subject = "Your Internship Application Has Been Rejected";
            body = String.format("""
                Dear %s,

                Unfortunately, your application for the internship position "%s" has been rejected.
                You can apply for other available positions.

                Best regards,
                Internship Management System
                """, studentUsername, position);
        } else {
            return;
        }

        sendEmail(studentEmail, subject, body);

        System.out.println("✅ Mail sent to student (" + status + "): " + studentEmail);
    }
}