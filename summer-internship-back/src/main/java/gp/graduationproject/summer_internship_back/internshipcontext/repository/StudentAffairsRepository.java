package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.StudentAffair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentAffairsRepository extends JpaRepository<StudentAffair,Integer> {
}
