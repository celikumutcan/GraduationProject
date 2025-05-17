import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { DarkModeService } from '../../services/dark-mode.service';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
})
export class WelcomeComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';
  showPassword: boolean = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private userService: UserService,
    public darkModeService: DarkModeService
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

            // ❌ Şirket kullanıcıları bu ekrandan giriş yapamasın
            if (response.user_type === 'company_branch') {
              this.errorMessage = 'Company users must log in from the Company Login screen.';
              return;
            }

            // ✅ Kullanıcı bilgilerini kaydet
            this.userService.setUser({
              userId: response.user_id,
              userName: response.user_name,
              firstName: response.first_name,
              lastName: response.last_name,
              email: response.email,
              userType: response.user_type,
            });

            if (response.token) {
              localStorage.setItem('token', response.token);
            }

            // ✅ Kullanıcı tipine göre yönlendirme
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
              default:
                this.errorMessage = 'Unknown user type.';
                break;
            }

          } else {
            this.errorMessage = 'Invalid credentials.';
          }
        },
        error: (err) => {
          console.error('You have entered wrong username or password.', err);
          this.errorMessage = 'You have entered wrong username or password.';
        },
      });
  }

  togglePassword() {
    const passwordField = document.getElementById('password') as HTMLInputElement;
    passwordField.type = passwordField.type === 'password' ? 'text' : 'password';
  }

  toggleShowPassword(): void {
    this.showPassword = !this.showPassword;
  }

  toggleDarkMode() {
    this.darkModeService.toggleDarkMode();
  }
}
