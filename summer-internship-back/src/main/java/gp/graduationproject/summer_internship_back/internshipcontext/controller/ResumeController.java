package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Resume;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /** Constructor to inject the service. */
    @Autowired
    public ResumeController(ResumeService resumeService)
    {
        this.resumeService = resumeService;
    }

    /** Get all resumes. */
    @GetMapping
    public List<Resume> getAllResumes()
    {
        return resumeService.getAllResumes();
    }

    /** Upload a resume. */
    @PostMapping("/upload")
    public Resume uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("userName") String userName) throws IOException
    {
        return resumeService.addResume(new String(file.getBytes()), userName);
    }

    /** Delete a resume by ID. */
    @DeleteMapping("/{id}")
    public void deleteResume(@PathVariable Integer id)
    {
        resumeService.deleteResume(id);
    }
}