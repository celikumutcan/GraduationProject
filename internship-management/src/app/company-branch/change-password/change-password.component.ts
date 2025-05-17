import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  newPassword: string = '';
  confirmPassword: string = '';

  constructor(private http: HttpClient) {}

  changePassword(): void {
    const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
    const username = currentUser?.userName;

    if (!username) {
      alert('User not found');
      return;
    }

    if (this.newPassword !== this.confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    const payload = {
      username,
      newPassword: this.newPassword,
      confirmPassword: this.confirmPassword
    };

    this.http.post('/api/company-branch/change-password', payload, { responseType: 'text' }).subscribe({
      next: (res: any) => {
        alert(res);
        this.newPassword = '';
        this.confirmPassword = '';
      },
      error: (err: any) => {
        alert(err.error || 'Error updating password');
      }
    });
  }
}
