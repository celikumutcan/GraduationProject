import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {TraineeInformationFormService} from '../../../services/trainee-information-form.service';
import {HttpClient} from '@angular/common/http';
import {DarkModeService} from '../../../services/dark-mode.service';
import {UserService} from '../../../services/user.service';
import {Router} from '@angular/router';
import {AssignInstructorService} from '../../../services/assign-instructor.service';
import {data} from 'autoprefixer';

@Component({
  selector: 'app-assign-instructors',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assign-instructors.component.html',
  styleUrl: './assign-instructors.component.css'
})
export class AssignInstructorsComponent  implements OnInit{
  userName = '';
  userFirstName = '';
  userLastName = '';
  initialForms: any[] = [];
  approvedForms: any[] = [];
  sortedForms: any[] = []; // To store all forms sorted by datetime
  selectedForm: any = null;
  instructors: any[] = [];
  searchQuery: string = '';
  isSuccessVisible = false;
  isAssigning = false;

  constructor(private http: HttpClient,
              private darkModeService: DarkModeService,
              private userService: UserService,
              private traineeInformationFormService: TraineeInformationFormService,
              private router: Router,
              private assignInstructorService: AssignInstructorService) {}

  ngOnInit(): void {
    this.userName = this.userService.getUser().userName;
    this.userFirstName = this.userService.getUser().firstName;
    this.userLastName = this.userService.getUser().lastName;
    this.fetchInstructors();
    this.fetchCoordinatorTraineeInformationForms();

  }

  fetchInstructors(): void {
    this.assignInstructorService.getInstructors().subscribe({
      next: (data: any[]) => {
        this.instructors = data;
        if (this.approvedForms.length) {
          this.injectSelectedInstructors();
        }
      },
      error: (err) => {
        console.log("Error fetching Instructors");
      }
    });
  }

  injectSelectedInstructors(): void {
    const allForms = this.approvedForms;

    for (const form of allForms) {
      if (form.evaluatingFacultyMember == 'defaultEvaluator' || form.evaluatingFacultyMember == null) {
        form.selectedInstructor = null;
      } else {
        form.selectedInstructor = this.instructors.find(
          instructor => instructor.userName === form.evaluatingFacultyMember
        ) || null;
      }
    }
  }

  fetchCoordinatorTraineeInformationForms(): void {
    this.traineeInformationFormService.getCoordinatorTraineeForms().subscribe({
      next: (data: [any[], any[]]) => {
        this.approvedForms = data[1];
        if (this.instructors.length) {
          this.injectSelectedInstructors();
        }
        this.combineAndSortForms();
      },
      error: (err) => {
        console.error('Error fetching Trainee Information Forms', err);
      }
    });
  }


  combineAndSortForms(): void {
    const combinedForms = [...this.initialForms, ...this.approvedForms];

    const filteredForms = combinedForms.filter(form => form.type !== 'Voluntary');

    this.sortedForms = filteredForms.sort(
      (a, b) => new Date(b.datetime).getTime() - new Date(a.datetime).getTime()
    );

    console.log(this.sortedForms);
  }

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

//
  assignInstructor(form: any): void {
    const formId = form.id;
    const selectedInstructor = form.selectedInstructor.userName;
    document.body.style.cursor = 'wait'

    this.assignInstructorService.assignInstructorManually(formId, selectedInstructor)
      .subscribe({
        next: (response) => {
          this.fetchInstructors();
          this.fetchCoordinatorTraineeInformationForms();
          console.log('Instructor assigned successfully:', response);
          this.isSuccessVisible = true;
          this.isAssigning = false;
          document.body.style.cursor = 'default';
        },
        error: (error) => {
          console.error('Error assigning instructor:', error);
          this.fetchInstructors();
          this.fetchCoordinatorTraineeInformationForms();
          this.isAssigning = false;
          document.body.style.cursor = 'default';
        },
      });

  }
  closeSuccess() {
    this.isSuccessVisible = false;
  }

  assignforms: any[] = [];
  randomAssign():void{
    document.body.style.cursor = 'wait'
    const instructors = this.instructors.map(instructor => instructor.userName);
    console.log(instructors);
    this.assignforms = this.sortedForms.filter(
      form =>
        form.evaluatingFacultyMember == 'defaultEvaluator' ||
        form.evaluatingFacultyMember == null
    );
    const assignFormIds = this.assignforms.map(form => form.id);
    this.assignInstructorService.assignInstructorRandomly(assignFormIds, instructors)
      .subscribe({
        next: (response) => {
          this.fetchInstructors();
          this.fetchCoordinatorTraineeInformationForms();
          console.log('Instructor assigned successfully:', response);
          this.isSuccessVisible = true;
          this.isAssigning = false;
          document.body.style.cursor = 'default';
        },
        error: (error) => {
          console.error('Error assigning instructor:', error);
          this.fetchInstructors();
          this.fetchCoordinatorTraineeInformationForms();
          this.isAssigning = false;
          document.body.style.cursor = 'default';
        },
      });


  }


}
