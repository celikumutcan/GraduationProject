package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.AcademicStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcademicStaffRepository extends JpaRepository<AcademicStaff, String> {
    Optional<AcademicStaff> findByUserName(String userName);
    List<AcademicStaff> findByCflagTrue();
    List<AcademicStaff> findByIflagTrue();
}