package gp.graduationproject.summer_internship_back.infrastructure.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"gp.graduationproject.summer_internship_back.internshipcontext.repository"})
@EnableTransactionManagement
@EnableJpaAuditing
public class DatabaseConfiguration {



}
