package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Resume;
import gp.graduationproject.summer_internship_back.internshipcontext.service.FileStorageService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FileStorageService fileStorageService;

    /** Constructor to inject the service. */
    @Autowired
    public ResumeController(ResumeService resumeService,FileStorageService fileStorageService)
    {
        this.resumeService = resumeService;
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
        return ResponseEntity.ok("CV Successfully Uploaded: " + fileName);
    }

    /** Delete a resume by ID. */
    @DeleteMapping("/{id}")
    public void deleteResume(@PathVariable Integer id)
    {
        resumeService.deleteResume(id);
    }
}