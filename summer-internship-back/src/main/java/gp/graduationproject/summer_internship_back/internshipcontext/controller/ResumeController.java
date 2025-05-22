package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Resume;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ResumeRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.service.FileStorageService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.ResumeService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ResumeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for managing resumes.
 */
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeRepository resumeRepository;
    private FileStorageService fileStorageService;

    /**
     * Constructor to inject services and repository.
     */
    @Autowired
    public ResumeController(ResumeService resumeService, FileStorageService fileStorageService, ResumeRepository resumeRepository) {
        this.resumeService = resumeService;
        this.resumeRepository = resumeRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Retrieves all resumes as DTOs.
     *
     * @return List of ResumeDTO
     */
    @GetMapping
    public List<ResumeDTO> getAllResumes() {
        return resumeService.getAllResumes()
                .stream()
                .map(resume -> new ResumeDTO(resume.getId(), resume.getUserName(), resume.getFileName(), resume.getFileType()))
                .collect(Collectors.toList());
    }

    /**
     * Uploads a CV for a specific student.
     *
     * @param username The username of the student
     * @param file     The uploaded resume file
     * @return Response with upload result
     */
    @PostMapping("/upload-cv/{username}")
    public ResponseEntity<?> uploadStudentCv(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeStudentCv(file, username);
        Map<String, String> response = new HashMap<>();

        try {
            resumeService.saveResume(username, file);
            response.put("message", "CV uploaded successfully");
            response.put("fileName", fileName);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("error", "Failed to upload and save CV");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Downloads a resume by resume ID.
     *
     * @param resumeId The ID of the resume
     * @return The resume file
     */
    @GetMapping("/download-cv-by-id/{resumeId}")
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

    /**
     * Deletes a resume by its ID.
     *
     * @param id The ID of the resume
     */
    @DeleteMapping("/{id}")
    public void deleteResume(@PathVariable Integer id) {
        System.out.println("Requested for deleting resume : " + id);
        resumeService.deleteResume(id);
    }

    /**
     * Downloads the resume file for the given student username.
     *
     * @param studentUsername the username of the student
     * @return resume file as ResponseEntity
     */
    @GetMapping("/download-cv/{studentUsername}")
    public ResponseEntity<Resource> downloadCvByStudentUsername(@PathVariable String studentUsername) {
        Resume resume = resumeRepository.findTopByUserName_UserNameOrderByIdDesc(studentUsername)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        ByteArrayResource resource = new ByteArrayResource(resume.getFileData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
