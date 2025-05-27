package gp.graduationproject.summer_internship_back;

import gp.graduationproject.summer_internship_back.internshipcontext.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
public class SummerInternshipBackApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(SummerInternshipBackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Şimdilik boş, burada bir şey çalıştırmana gerek yok.
	}
}