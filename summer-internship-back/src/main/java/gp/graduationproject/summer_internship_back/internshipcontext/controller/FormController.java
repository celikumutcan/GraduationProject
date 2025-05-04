package gp.graduationproject.summer_internship_back.internshipcontext.controller;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Form;
import gp.graduationproject.summer_internship_back.internshipcontext.service.FormService;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.FormDTO;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * REST controller for managing Form entities.
 * Provides endpoints to create, retrieve, and delete forms.
 */
@RestController
@RequestMapping("/api/forms")
public class FormController {

    private final FormService formService;

    /**
     * Constructs a new FormController with the given FormService.
     *
     * @param formService The service layer for managing form operations.
     */
    public FormController(FormService formService)
    {
        this.formService = formService;
    }

    /**
     * Creates a new form with the provided file, content, and user information.
     *
     * @param file        The file content as a string.
     * @param content     Additional content for the form.
     * @param addUserName The username of the academic staff adding the form.
     * @return The created Form entity.
     */
    @PostMapping
    public Form createForm(@RequestBody String file, @RequestParam String content, @RequestParam String addUserName)
    {
        return formService.addForm(file, content, addUserName);
    }

    /**
     * Retrieves all forms as FormDTOs.
     *
     * @return A list of all FormDTOs.
     */
    @GetMapping
    public List<FormDTO> getAllForms()
    {
        return formService.getAllFormsDTO();
    }


    /**
     * Deletes a form by its ID.
     *
     * @param id The ID of the form to delete.
     */
    @DeleteMapping("/{id}")
    public void deleteForm(@PathVariable Integer id)
    {
        formService.deleteForm(id);
    }


    /**
     * Uploads a file and creates a new form.
     *
     * @param file        The uploaded file as a MultipartFile.
     * @param content     Additional content for the form.
     * @param addUserName The username of the academic staff adding the form.
     * @return The created Form entity.
     * @throws IOException If there is an error reading the file content.
     */
    @PostMapping("/upload")
    public Form uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("content") String content, @RequestParam("addUserName") String addUserName) throws IOException
    {
        String fileContent = new String(file.getBytes()); // Convert file bytes to a string
        return formService.addForm(fileContent, content, addUserName);
    }

    /**
     * Downloads a form file as PDF.
     *
     * @param id The ID of the form to download.
     * @return The PDF file as a byte array in the response.
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadForm(@PathVariable Integer id)
    {
        Form form = formService.getFormById(id);
        byte[] pdfBytes = form.getFile().getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("form_" + id + ".pdf")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}