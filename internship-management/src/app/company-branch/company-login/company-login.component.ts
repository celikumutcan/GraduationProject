import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-company-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './company-login.component.html',
  styleUrls: ['./company-login.component.css']
})
export class CompanyLoginComponent {
  username: string = '';
  password: string = '';
  resetUserName: string = '';
  showPassword: boolean = false;
  showReset: boolean = false;

  errorMessage: string = '';
  successMessage: string = '';

  constructor(private http: HttpClient) {}

  toggleShowPassword(): void {
    this.showPassword = !this.showPassword;
  }

  onLogin(): void {
    if (!this.username.trim() || !this.password.trim()) {
      Swal.fire('Error', 'Please fill in both username and password.', 'error');
      return;
    }

    const loginData = { username: this.username, password: this.password };

    this.http.post<any>('http://localhost:8080/auth/verify', loginData)
      .subscribe({
        next: (response) => {
          if (response.success && response.user_type === 'company_branch') {
            localStorage.setItem('token', response.token);
            localStorage.setItem('currentUser', JSON.stringify({
              userId: response.user_id,
              userName: response.user_name,
              firstName: response.first_name,
              lastName: response.last_name,
              email: response.email,
              userType: response.user_type
            }));

            window.location.href = '/company-branch';

          } else {
            Swal.fire('Error', 'Invalid credentials or unauthorized user type.', 'error');
          }
        },
        error: () => {
          Swal.fire('Error', 'Login failed. Please try again.', 'error');
        }
      });
  }

  sendResetPassword() {
    if (!this.resetUserName.trim()) {
      Swal.fire('Error', 'Please enter your branch username.', 'error');
      return;
    }

    this.http.post('/api/company-branch/send-password', {
      branch_user_name: this.resetUserName
    }, { responseType: 'text' })
      .subscribe({
        next: () => {
          Swal.fire('Success', 'Password has been sent to your email.', 'success');
          this.resetUserName = '';
          this.showReset = false;
        },
        error: (err) => {
          Swal.fire('Error', err?.error || 'Failed to send password.', 'error');
        }
      });
  }
}
