package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Report;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report addReport(Report report) {
        return reportRepository.save(report); // Saves or updates a report
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll(); // Retrieves all reports
    }

    public Report getReportById(Integer id) {
        return reportRepository.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
    }

    public void deleteReport(Integer id) {
        reportRepository.deleteById(id); // Deletes a report by ID
    }
}

