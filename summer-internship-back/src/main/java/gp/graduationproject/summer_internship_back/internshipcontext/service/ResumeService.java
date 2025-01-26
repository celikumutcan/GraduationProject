package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Resume;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.Student;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ResumeRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing resumes.
 */
@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final StudentRepository studentRepository;

    /**
     * Constructor to inject repositories.
     *
     * @param resumeRepository  Repository for Resume entity.
     * @param studentRepository Repository for Student entity.
     */
    public ResumeService(ResumeRepository resumeRepository, StudentRepository studentRepository)
    {
        this.resumeRepository = resumeRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Adds a new resume.
     *
     * @param file     File content of the resume.
     * @param userName The username of the student.
     * @return The saved Resume entity.
     */
    public Resume addResume(String file, String userName)
    {
        Student student;

        try {
            if (studentRepository.findByUserName(userName).isPresent())
            {
                student = studentRepository.findByUserName(userName).get();
            }
            else
            {
                throw new RuntimeException("Student not found with username: " + userName);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving the student: " + userName, e);
        }

        Resume resume = new Resume();
        resume.setFile(file);
        resume.setUserName(student);
        return resumeRepository.save(resume);
    }

    /**
     * Retrieves all resumes.
     *
     * @return List of all resumes.
     */
    public List<Resume> getAllResumes()
    {
        return resumeRepository.findAll();
    }

    /**
     * Deletes a resume by its ID.
     *
     * @param id The ID of the resume to delete.
     */
    public void deleteResume(Integer id)
    {
        try {
            if (!resumeRepository.existsById(id)) {
                throw new RuntimeException("Resume not found with ID: " + id);
            }
            resumeRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting the resume with ID: " + id, e);
        }
    }
}