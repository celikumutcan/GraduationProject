package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Resume;
import gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ResumeListItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Resume} entities.

 * Provides basic CRUD operations for the Resume table in the database.
 * This interface extends {@link JpaRepository}, inheriting methods to interact with the database.
 */
public interface ResumeRepository extends JpaRepository<Resume, Integer> {
    Optional<Resume> findTopByUserName_UserNameOrderByIdDesc(String userName);

    @Query("SELECT new gp.graduationproject.summer_internship_back.internshipcontext.service.dto.ResumeListItemDTO(" +
            "r.id, r.fileName, r.fileType, r.userName.userName) FROM Resume r")
    List<ResumeListItemDTO> findAllResumeListItemDTOs();
}
