import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EvaluateInternStudentService, InternStudent } from '../../../services/evaluate-intern-student.service';
import { UserService } from '../../../services/user.service';
import {TraineeInformationFormService} from '../../../services/trainee-information-form.service';
import {CompanyService} from '../../../services/company.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-evaluate-intern-student',
  standalone: true,
  imports: [CommonModule, FormsModule],
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
  toastMessage: string | null = null;

  //Supervisor variables
  selectedform:any = null;
  showSupervisorModal = false;
  name: string = '';
  surname: string = '';

  //Evaluation variables
  isModalOpen = false;
  comments: string = '';
  evaluatingform:any=null;
  attendance: string = '';
  diligence: string = '';
  contribution: string = '';
  performance: string = '';

  constructor(
    private evaluateService: EvaluateInternStudentService,
    private traineeFormService: TraineeInformationFormService,
    private companyService: CompanyService,
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

  onApproveInternship(internshipId: number): void {
    this.showToast("Internship successfully approved!");

    this.companyService.approveInternship(internshipId).subscribe({
      next: response => {
        console.log('Internship approved:', response);
        this.getCompanyTraineeForms();

      },
      error: err => {
        console.error('Error approving internship:', err);
      }
    });
  }

  onRejectInternship(internshipId: number): void {
    this.showToast("Internship successfully rejected!");

    this.companyService.rejectInternship(internshipId).subscribe({
      next: response => {
        console.log('Internship rejected:', response);
        this.getCompanyTraineeForms();

      },
      error: err => {
        console.error('Error rejecting internship:', err);
      }
    });
  }

  openSupervisorModal(selectedForm:any) {
    this.selectedform = selectedForm;
    this.showSupervisorModal = true;
  }

  closeSupervisorModal() {
    this.showSupervisorModal = false;
  }

  onSubmit() {
    setTimeout(() => {
      this.closeSupervisorModal();
    }, 1500);


    this.companyService.updateSupervisor(this.selectedform.id, this.name, this.surname).subscribe({
      next: (res) => {
        this.getCompanyTraineeForms();
      },
      error: () => {
        this.getCompanyTraineeForms();
      }
    });
  }

  private showToast(message: string) {
    this.toastMessage = message;
    setTimeout(() => {
      this.toastMessage = null;
    }, 3000); // Hide toast after 3 seconds
  }



  openEvaluationModal(form:any) {
    this.evaluatingform = form;

    this.companyService.getEvaluationByTraineeFormId(form.id).subscribe({
      next: (res) => {
        if (res.length > 0) {
          const data = res[0];
          this.attendance = data.attendance;
          this.diligence = data.diligenceAndEnthusiasm;
          this.contribution = data.contributionToWorkEnvironment;
          this.performance = data.overallPerformance;
          this.comments = data.comments;
        }
        this.isModalOpen = true;
      },
      error: (err) => {
        console.error('Failed to fetch evaluation data', err);
        this.isModalOpen = true; // still open modal even if fetch fails
      }
    });
  }


  closeEvaluationModal() {
    this.isModalOpen = false;
    this.comments = '';
    this.evaluatingform=null;
    this.attendance= '';
    this.diligence= '';
    this.contribution = '';
    this.performance = '';
  }

  submitEvaluation() {
    this.closeEvaluationModal();
    this.companyService.createEvaluationForm({
      traineeFormId: this.evaluatingform.id,
      attendance: this.attendance,
      diligenceAndEnthusiasm: this.diligence,
      contributionToWorkEnvironment: this.contribution,
      overallPerformance: this.performance,
      comments: this.comments
    }).subscribe({
      next: (response) => {
        this.comments = '';
        this.evaluatingform=null;
        this.attendance= '';
        this.diligence= '';
        this.contribution = '';
        this.performance = '';
        
        this.showToast("Evaluation successfully submitted!");
        console.log(response);
      },
      error: (err) => {
        console.error('Failed to submit evaluation', err);
      }
    });
  }

}
