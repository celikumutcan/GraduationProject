import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DarkModeService } from '../../../services/dark-mode.service';
import { UserService } from '../../../services/user.service';
import { TraineeInformationFormService } from '../../../services/trainee-information-form.service';
import { Router } from '@angular/router';
import {EvaluateReportsService} from '../../../services/evaluate-reports.service'; // Import your service

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

  constructor(
    private darkModeService: DarkModeService,
    private evaluateReportsService: EvaluateReportsService,
    private userService: UserService,
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
  dates:any[] = [];
  // Get selected forms when needed
  getSelectedForms(): void {
    if(this.selectedForms.length == 0){
      alert("Please Select Forms to Download");
    }
    else{

      this.dates = [];
      this.selectedForms.forEach((value: any) => {
        this.dates.push(value.internshipEndDate);
      });

      this.dates.sort((a, b) => new Date(a).getTime() - new Date(b).getTime());
      this.evaluateReportsService.downloadExcel(
        this.userName,
        new Date(this.dates[0]).toISOString(),
        new Date().toISOString()
      ).subscribe({
        next: (response: Blob) => {
          const blob = new Blob([response], { type: response.type });

          // Create a download link
          const link = document.createElement("a");
          link.href = window.URL.createObjectURL(blob);
          link.download = "report.xlsx"; // Change extension if it's PDF
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        },
        error: (err) => {
          console.error("Error downloading the file:", err);
        }
      });



    }

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
    alert('Form rejected successfully!');
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
}
