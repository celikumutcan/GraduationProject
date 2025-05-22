import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EvaluateInternStudentService, InternStudent } from '../../../services/evaluate-intern-student.service';
import { UserService } from '../../../services/user.service';
import {TraineeInformationFormService} from '../../../services/trainee-information-form.service';

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
  forms: any[] = [];
  selectedForm:any;

  constructor(
    private evaluateService: EvaluateInternStudentService,
    private traineeFormService: TraineeInformationFormService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const currentUser = this.userService.getUser();
    this.companyUserName = currentUser?.userName || '';
    this.getCompanyTraineeForms();
  }
  getCompanyTraineeForms(): void{
    this.traineeFormService.getCompanyTraineeForms(this.companyUserName)
      .subscribe({
        next: (data) => {
          console.log("success",data);
          this.forms = data;
        },
        error: (err) => {
          console.error('Error fetching intern students:', err);
          this.errorMessage = 'Error fetching intern students.';
          this.loading = false;
        }
      });
  }
  openDetails(form1: any) {
    this.selectedForm = form1;
  }

}
