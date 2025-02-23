package gp.graduationproject.summer_internship_back.internshipcontext.service;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.ApprovedInternship;
import gp.graduationproject.summer_internship_back.internshipcontext.domain.InternshipAssignment;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.AcademicStaffRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.ApprovedInternshipRepository;
import gp.graduationproject.summer_internship_back.internshipcontext.repository.InternshipAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class InternshipAssignmentService {
    private final AcademicStaffRepository academicStaffRepository;
    private final ApprovedInternshipRepository approvedInternshipRepository;
    private final InternshipAssignmentRepository internshipAssignmentRepository;

    @Autowired
    public InternshipAssignmentService(AcademicStaffRepository academicStaffRepository,
                                       ApprovedInternshipRepository approvedInternshipRepository,
                                       InternshipAssignmentRepository internshipAssignmentRepository) {
        this.academicStaffRepository = academicStaffRepository;
        this.approvedInternshipRepository = approvedInternshipRepository;
        this.internshipAssignmentRepository = internshipAssignmentRepository;
    }

    /**
     * 📌 Stajları eğitmenlere eşit dağıtır
     */
    public String assignInternshipsToInstructors() {
        List<AcademicStaff> instructors = academicStaffRepository.findAll();
        List<ApprovedInternship> unassignedInternships = approvedInternshipRepository.findByAssignedFalse();

        //BURAYA O TABLOYA ASSIGN OLAYI EKLENMELI TAMAMEN
        if (instructors.isEmpty() || unassignedInternships.isEmpty()) {
            return "❌ Atanacak eğitmen veya staj bulunamadı.";
        }

        int instructorIndex = 0;

        for (ApprovedInternship internship : unassignedInternships) {
            AcademicStaff assignedInstructor = instructors.get(instructorIndex);

            InternshipAssignment assignment = new InternshipAssignment();
            assignment.setInternship(internship);
            assignment.setInstructor(assignedInstructor);
            assignment.setAssignedDate(Instant.now());

            internship.setAssigned(true);
            internship.setAssignedInstructor(assignedInstructor);

            internshipAssignmentRepository.save(assignment);
            approvedInternshipRepository.save(internship);

            instructorIndex = (instructorIndex + 1) % instructors.size(); // 📌 Round-robin algoritması
        }

        return "✅ Stajlar eğitmenlere başarıyla atandı.";
    }
}
