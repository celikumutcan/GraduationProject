package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
    Optional<Deadline> findFirstByOrderByIdDesc();
}
