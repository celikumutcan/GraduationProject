package gp.graduationproject.summer_internship_back.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()  // Disable CSRF for non-browser clients (e.g., APIs)
                .authorizeRequests()
                .requestMatchers("/auth/verify").permitAll()  // Allow unauthenticated access to /auth/verify
                //.anyRequest().authenticated()  // Secure other endpoints
                .and()
                .formLogin()  // Enable form login (if needed)
                .permitAll()
                .and()
                .httpBasic();  // Enable basic authentication (optional)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use BCrypt for password encryption
    }
}



