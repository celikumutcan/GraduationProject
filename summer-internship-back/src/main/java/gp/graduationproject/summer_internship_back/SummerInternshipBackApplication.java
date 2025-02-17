package gp.graduationproject.summer_internship_back;

import gp.graduationproject.summer_internship_back.internshipcontext.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SummerInternshipBackApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(SummerInternshipBackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}