package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Resume;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ResumeRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.FileStorageService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controller for managing resumes.
 */
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeRepository resumeRepository;
    private FileStorageService fileStorageService;

    /** Constructor to inject the service. */
    @Autowired
    public ResumeController(ResumeService resumeService, FileStorageService fileStorageService, ResumeRepository resumeRepository)
    {
        this.resumeService = resumeService;
        this.resumeRepository = resumeRepository;
        this.fileStorageService = fileStorageService;
    }

    /** Get all resumes. */
    @GetMapping
    public List<Resume> getAllResumes()
    {
        return resumeService.getAllResumes();
    }

    @PostMapping("/upload-cv/{username}")
    public ResponseEntity<?> uploadStudentCv(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeStudentCv(file, username);
        try {
            resumeService.saveResume(username, file);
            return ResponseEntity.ok("CV uploaded and saved successfully: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload and save CV");
        }
    }

    @GetMapping("/download-cv/{resumeId}")
    public ResponseEntity<?> downloadResume(@PathVariable Integer resumeId) {
        return resumeRepository.findById(resumeId)
                .map(resume -> {
                    byte[] data = resume.getFileData();
                    ByteArrayResource resource = new ByteArrayResource(data);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf")
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /** Delete a resume by ID. */
    @DeleteMapping("/{id}")
    public void deleteResume(@PathVariable Integer id)
    {
        resumeService.deleteResume(id);
    }
}