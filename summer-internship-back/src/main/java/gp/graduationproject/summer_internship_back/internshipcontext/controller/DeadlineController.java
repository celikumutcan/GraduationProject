package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Deadline;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.DeadlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/deadlines")
public class DeadlineController {

    @Autowired
    private final DeadlineRepository deadlineRepository;

    public DeadlineController(DeadlineRepository deadlineRepository) {
        this.deadlineRepository = deadlineRepository;
    }
    // 📌 ✅ Deadline ekleme endpoint'i
    @PostMapping("/add")
    public ResponseEntity<?> addDeadline(@RequestBody Map<String, String> payload) {
        try {
            String internshipDeadlineStr = payload.get("internshipDeadline");
            String reportDeadlineStr = payload.get("reportDeadline"); // ✅ Rapor tarihi opsiyonel

            if (internshipDeadlineStr == null || internshipDeadlineStr.isEmpty()) {
                return ResponseEntity.status(400).body(Map.of("error", "Internship deadline is required"));
            }

            LocalDate internshipDeadline = LocalDate.parse(internshipDeadlineStr);
            LocalDate reportDeadline = reportDeadlineStr != null && !reportDeadlineStr.isEmpty()
                    ? LocalDate.parse(reportDeadlineStr)
                    : null; // Eğer boşsa null ata

            Deadline deadline = new Deadline();
            deadline.setInternshipDeadline(internshipDeadline);
            deadline.setReportDeadline(reportDeadline); // ✅ Opsiyonel olarak ayarlandı

            deadlineRepository.save(deadline);

            return ResponseEntity.ok(Map.of("message", "Deadline added successfully", "deadline", internshipDeadline));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }

    @GetMapping("/latestDeadline")
    public ResponseEntity<Map<String, Object>> getLatestDeadline() {
        Optional<Deadline> latestDeadlineOpt = deadlineRepository.findFirstByOrderByIdDesc();

        if (latestDeadlineOpt.isPresent()) {
            Deadline deadline = latestDeadlineOpt.get();
            LocalDate deadlineDate = deadline.getInternshipDeadline();
            LocalDate today = LocalDate.now();

            long daysRemaining = ChronoUnit.DAYS.between(today, deadlineDate);

            Map<String, Object> response = new HashMap<>();
            response.put("deadline", deadlineDate);
            response.put("daysRemaining", daysRemaining);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "No deadline found"));
        }
    }
    @PostMapping
    public ResponseEntity<String> setDeadline(@RequestBody Deadline deadline) {
        deadlineRepository.save(deadline);
        return ResponseEntity.ok("Deadlines saved successfully.");
    }


}
