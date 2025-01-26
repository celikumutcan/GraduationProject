package gp.graduationproject.summer_internship_back.internshipcontext.repository;

import gp.graduationproject.summer_internship_back.internshipcontext.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
