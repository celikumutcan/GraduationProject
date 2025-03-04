import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { DarkModeService } from '../../services/dark-mode.service';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [RouterModule, FormsModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
})
export class WelcomeComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private userService: UserService,
    public darkModeService: DarkModeService // DarkModeService'i ekledik
  ) {}

  onSubmit() {
    this.login(this.username, this.password);
  }

  login(username: string, password: string) {
    const loginData = { username, password };

    this.http.post<any>('http://localhost:8080/auth/verify', loginData)
      .subscribe({
        next: (response) => {
          if (response.success) {
            // Kullanıcı bilgilerini kaydet
            this.userService.setUser({
              userId: response.user_id,
              userName: response.user_name,
              firstName: response.first_name,
              lastName: response.last_name,
              email: response.email,
              userType: response.user_type,
            });

            // **TOKEN'ı localStorage'a kaydet**
            if (response.token) {
              localStorage.setItem('token', response.token);
            }

            // Kullanıcı tipine göre yönlendirme
            switch (response.user_type) {
              case 'student':
                this.router.navigate(['/student']);
                break;
              case 'coordinator':
                this.router.navigate(['/coordinator']);
                break;
              case 'instructor':
                this.router.navigate(['/instructor']);
                break;
              case 'student_affairs':
                this.router.navigate(['/student-affairs']);
                break;
              case 'company_branch':
                this.router.navigate(['/company-branch']);
                break;
              default:
                this.errorMessage = 'Unknown user type.';
                break;
            }
          } else {
            this.errorMessage = 'Invalid credentials.';
          }
        },
        error: (err) => {
          console.error('Error accessing backend.', err);
          this.errorMessage = 'Error accessing backend.';
        },
      });
  }

  togglePassword() {
    const passwordField = document.getElementById('password') as HTMLInputElement;
    passwordField.type = passwordField.type === 'password' ? 'text' : 'password';
  }

  // Dark mode toggle fonksiyonu
  toggleDarkMode() {
    this.darkModeService.toggleDarkMode();
  }
}