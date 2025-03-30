import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EvaluateInternStudentService, InternStudent } from '../../../services/evaluate-intern-student.service';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-evaluate-intern-student',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './evaluate-intern-student.component.html',
  styleUrls: ['./evaluate-intern-student.component.css']
})
export class EvaluateInternStudentComponent implements OnInit {
  internStudents: InternStudent[] = [];
  loading: boolean = false;
  errorMessage: string = '';
  companyUserName: string = '';

  constructor(
    private evaluateService: EvaluateInternStudentService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const currentUser = this.userService.getUser();
    this.companyUserName = currentUser?.userName || '';
    this.fetchInternStudents();
  }

  fetchInternStudents(): void {
    this.loading = true;
    this.errorMessage = '';
    this.evaluateService.getInternStudents(this.companyUserName)
      .subscribe({
        next: (data) => {
          this.internStudents = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Error fetching intern students:', err);
          this.errorMessage = 'Error fetching intern students.';
          this.loading = false;
        }
      });
  }
}
