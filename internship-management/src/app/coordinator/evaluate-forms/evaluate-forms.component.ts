import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DarkModeService } from '../../../services/dark-mode.service';
import {UserService} from '../../../services/user.service';
import {TraineeInformationFormService} from '../../../services/trainee-information-form.service';
import {Router} from '@angular/router'; // Import your service

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

  userName = '';
  userFirstName ='';
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


  constructor(private darkModeService: DarkModeService,
              private userService: UserService,
              private traineeInformationFormService: TraineeInformationFormService,
              private router: Router) {} // Inject the service

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

  approveForm(index: number): void {
    this.forms[index].coordinatorStatus = 'Approved';
    this.saveForms();
    alert('Form approved successfully!');
  }

  rejectForm(index: number): void {
    this.forms[index].coordinatorStatus = 'Rejected';
    this.saveForms();
    alert('Form rejected successfully!');
  }

  takeBackAction(index: number): void {
    this.forms[index].coordinatorStatus = 'Pending';
    this.saveForms();
    alert('Action taken back successfully!');
  }

  viewDetails(form: FormData): void {
    //this.selectedForm = { ...form };
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
}
