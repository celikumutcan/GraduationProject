import { Component } from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import { DarkModeService } from '../../services/dark-mode.service';
import {FormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {UserService} from '../../services/user.service';
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

  constructor(private http: HttpClient, private router: Router, private userService: UserService) {}

  onSubmit() {
    this.login(this.username, this.password);
  }

  login(username: string, password: string) {
    const loginData = { username, password };

    this.http.post<any>('http://localhost:8080/auth/verify', loginData)
      .subscribe({
        next: (response) => {
          if (response.success) {
            // Save user details in UserService
            this.userService.setUser({
              userId: response.user_id,
              userName: response.user_name,
              firstName: response.first_name,
              lastName: response.last_name,
              email: response.email,
              userType: response.user_type,
            });

            // Navigate to the dashboard
            if(response.user_type == "student"){
              this.router.navigate(['/student']);
            }
            else if(response.user_type == "coordinator"){
              this.router.navigate(['/coordinator']);
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
}
