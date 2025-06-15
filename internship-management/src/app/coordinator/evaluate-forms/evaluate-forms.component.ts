import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DarkModeService } from '../../../services/dark-mode.service';
import { UserService } from '../../../services/user.service';
import { TraineeInformationFormService } from '../../../services/trainee-information-form.service';
import { Router } from '@angular/router';
import {EvaluateReportsService} from '../../../services/evaluate-reports.service';
import {HttpErrorResponse} from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import {CompanyService} from '../../../services/company.service'; // Import your service

interface FormData {
  studentNo: string;
  name: string;
  surname: string;
  course: string;
  idNo: string;
  dateOfBirth: string;
  nationality: string;
  telephone: string;
  department: string;
  registrationSemester: string;
  startDate: string;
  endDate: string;
  coordinatorStatus: 'Pending' | 'Approved' | 'Rejected';
}

@Component({
  selector: 'app-evaluate-forms',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './evaluate-forms.component.html',
  styleUrls: ['./evaluate-forms.component.css'],
})
export class EvaluateFormsComponent implements OnInit {
  forms: FormData[] = [];
  isFormVisible = false;
  isEditing = false;

  initialForms: any[] = [];
  approvedForms: any[] = [];
  sortedForms: any[] = []; // To store all forms sorted by datetime
  selectedForm: any = null;
  selectedForms: any[] = []; // Stores selected rows

  // EKLENTİ: Arama özelliği için model
  searchQuery: string = '';

  userName = '';
  userFirstName = '';
  userLastName = '';

  isAddingNewCompany = false;
  isAddingNewBranch = false;

  isDetailsVisible = false;
  formData = {
    code: '',
    startDate: '',
    endDate: '',
    company: '',
    branch: '',
    newCompany: {
      name: '',
    },
    newBranch: {
      name: '',
      country: '',
      address: '',
      tel: '',
      email: ''
    },
    position: '',
    type: '',
    healthInsurance: ''
  };

  evaluationStatusMap: { [key: number]: Observable<boolean> } = {};
  showCompanyEvaluation= false;

  comments: string = '';
  evaluatingform:any=null;
  attendance: string = '';
  diligence: string = '';
  contribution: string = '';
  performance: string = '';


  constructor(
    private darkModeService: DarkModeService,
    private evaluateReportsService: EvaluateReportsService,
    private userService: UserService,
    private companyService: CompanyService,
    private traineeInformationFormService: TraineeInformationFormService,
    private router: Router
  ) {} // Inject the service

  ngOnInit(): void {
    this.userName = this.userService.getUser().userName;
    this.userFirstName = this.userService.getUser().firstName;
    this.userLastName = this.userService.getUser().lastName;
    this.fetchCoordinatorTraineeInformationForms();
  }

  // Add a method to toggle dark mode
  toggleDarkMode(): void {
    this.darkModeService.toggleDarkMode();
  }

  // Add a getter to check if dark mode is enabled
  get isDarkMode(): boolean {
    return this.darkModeService.isDarkMode;
  }

  loadForms(): void {
    const storedForms = localStorage.getItem('traineeForms');
    try {
      this.forms = storedForms ? JSON.parse(storedForms) : [];
    } catch {
      this.forms = [];
    }
  }

  openDetails(form1: any) {
    this.selectedForm = form1;
  }

  closeModal() {
    this.selectedForm = null;
  }

  // Toggle selection of checkboxes
  toggleSelection(form: any): void {
    const index = this.selectedForms.indexOf(form);
    if (index === -1) {
      this.selectedForms.push(form); // Add if not selected
    } else {
      this.selectedForms.splice(index, 1); // Remove if already selected
    }
  }


  getSelectedForms(): void {
    const from = '2000-01-01';
    const to = new Date().toISOString().split("T")[0];

    this.evaluateReportsService.downloadExcelForCoordinator(from, to)
      .subscribe({
        next: (blob: Blob) => {
          const downloadLink = document.createElement('a');
          downloadLink.href = URL.createObjectURL(blob);
          downloadLink.download = `student_grades_${from}_${to}.xlsx`;
          downloadLink.click();
        },
        error: (err: HttpErrorResponse) => {
          console.error('Download failed', err);
          alert('Excel indirme sırasında bir hata oluştu.');
        }
      });
  }


  approveForm(form: any): void {
    this.traineeInformationFormService
      .coordinatorApproveStudentTraineeInformationForm(form.username, form.id)
      .subscribe({
        next: (response: any) => {
          if (
            (response && response.status === 201) ||
            (response && response.status === 200)
          ) {
            console.log('Form approved successfully', response);
            alert('Form approved successfully!');
            this.closeModal();
            this.fetchCoordinatorTraineeInformationForms();
          } else {
            alert('Form approved successfully!');
            this.closeModal();
            this.fetchCoordinatorTraineeInformationForms();
            console.warn('Unexpected response', response);
          }
        },
        error: (err) => {
          console.error('Error submitting the form', err);
          this.closeModal();
          this.fetchCoordinatorTraineeInformationForms();
        }
      });
  }

  rejectForm(form: any): void {
    console.log(form.endDate, form.position, form.username);
    this.traineeInformationFormService
      .coordinatorRejectStudentTraineeInformationForm(form.username, form.endDate, form.position)
      .subscribe({
        next: (response: any) => {
          if (
            (response && response.status === 201) ||
            (response && response.status === 200)
          ) {
            console.log('Form rejected successfully', response);
            alert('Form rejected successfully!');
            this.closeModal();
            this.fetchCoordinatorTraineeInformationForms();
          } else {
            alert('Form rejected successfully!');
            this.closeModal();
            this.fetchCoordinatorTraineeInformationForms();
            console.warn('Unexpected response', response);
          }
        },
        error: (err) => {
          console.error('Error submitting the form', err);
          this.closeModal();
          this.fetchCoordinatorTraineeInformationForms();
        }
      });
  }

  takeBackAction(index: number): void {
    this.forms[index].coordinatorStatus = 'Pending';
    this.saveForms();
    alert('Action taken back successfully!');
  }

  viewDetails(form: FormData): void {
    // this.selectedForm = { ...form };
    this.isDetailsVisible = true;
  }

  fetchCoordinatorTraineeInformationForms(): void {
    this.traineeInformationFormService.getCoordinatorTraineeForms().subscribe({
      next: (data: [any[], any[]]) => {
        this.initialForms = data[0];
        this.approvedForms = data[1];
        this.approvedForms.forEach(form => {
          this.evaluationStatusMap[form.id] = this.checkCompanyEvaluation(form.id);
        });
        this.combineAndSortForms();
      },
      error: (err) => {
        console.error('Error fetching Trainee Information Forms', err);
      }
    });
  }

  combineAndSortForms(): void {
    const combinedForms = [...this.initialForms, ...this.approvedForms];
    this.sortedForms = combinedForms.sort(
      (a, b) => new Date(b.datetime).getTime() - new Date(a.datetime).getTime()
    );
    console.log(this.sortedForms);
  }


  openCompanyEval(form2:any){
    this.showCompanyEvaluation = true;
    this.openCompanyEvaluation(form2.id);
  }

  openCompanyEvaluation(id:number){
    this.companyService.getEvaluationByTraineeFormId(id).subscribe({
      next: (res) => {
        console.log("id is:",id);
        if (res.length > 0) {
          const data = res[0];
          this.attendance = data.attendance;
          this.diligence = data.diligenceAndEnthusiasm;
          this.contribution = data.contributionToWorkEnvironment;
          this.performance = data.overallPerformance;
          this.comments = data.comments;
        }
        this.showCompanyEvaluation = true;
      },
      error: (err) => {
        console.error('Failed to fetch evaluation data', err);
        this.showCompanyEvaluation = true; // still open modal even if fetch fails
      }
    });
  }

  closeCompanyEvaluation(){
    this.comments = '';
    this.evaluatingform=null;
    this.attendance= '';
    this.diligence= '';
    this.contribution = '';
    this.performance = '';
    this.showCompanyEvaluation = false;


  }
  preventChange(event: Event): void {
    event.preventDefault(); // Prevent the click from changing the value
  }

  closeDetails(): void {
    this.isDetailsVisible = false;
  }

  saveForms(): void {
    localStorage.setItem('traineeForms', JSON.stringify(this.forms));
  }

  // EKLENTİ: Arama terimine göre formları filtreleyen getter
  get filteredForms(): any[] {
    if (!this.searchQuery) {
      return this.sortedForms;
    }
    const query = this.searchQuery.toLowerCase();
    return this.sortedForms.filter(form =>
      (form.name && form.name.toLowerCase().includes(query)) ||
      (form.lastName && form.lastName.toLowerCase().includes(query)) ||
      (form.username && form.username.toLowerCase().includes(query)) ||
      (form.code && form.code.toLowerCase().includes(query))
    );
  }

  checkCompanyEvaluation(id: number): Observable<boolean> {
    return this.companyService.getEvaluationByTraineeFormId(id).pipe(
      map(res => {
        console.log(res);
        if (res.length === 0) return false;
        const { attendance, comments } = res[0];
        return attendance != null || comments != null;
      }),
      catchError(err => {
        console.error('Failed to fetch evaluation data', err);
        return of(false);
      })
    );
  }
}
