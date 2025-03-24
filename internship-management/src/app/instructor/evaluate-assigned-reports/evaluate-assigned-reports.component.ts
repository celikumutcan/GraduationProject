import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import { EvaluateReportsService } from '../../../services/evaluate-reports.service';
import {UserService} from '../../../services/user.service';
import {TraineeInformationFormService} from '../../../services/trainee-information-form.service';
import {ReportService} from '../../../services/report.service';
import {FormsModule} from '@angular/forms'; // Servis yolunu projenize göre ayarlayın

export interface Report {
  studentName: string;
  studentId: string;
  course: string;
  reportStatus: string;
  professorComment?: string;
  assignedDate: string;
}

@Component({
  selector: 'app-evaluate-assigned-reports',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './evaluate-assigned-reports.component.html',
  styleUrls: ['./evaluate-assigned-reports.component.css']
})

export class EvaluateAssignedReportsComponent implements OnInit {
  forms: any[] = [];
  loading: boolean = false;
  selectedForm:any;
  reportsPage = false;
  reportsLoading = false;
  reports :any;
  reportFormId: number = 0;
  userName = "";
  selectedForms: any[] = []; // Stores selected rows

  showCompanyEvaluation=false;
  showGrading = false;
  selectedReport:any;

  selectedOption: string = '';

  constructor(
    private evaluateReportsService: EvaluateReportsService,
    private userService: UserService,
    private router: Router,
    private reportService: ReportService) {
  }

  ngOnInit(): void {
    this.userName = this.userService.getUser().userName;
    this.fetchReports();
  }

  fetchReports(): void {
    this.loading = true;
    this.evaluateReportsService.getCoordinatorTraineeForms().subscribe({
      next: (data:any) => {
        this.sortForms(data.reverse());
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching forms', err);
        this.loading = false;
      }
    });
  }

  openDetails(form1: any) {
    this.selectedForm = form1;
  }

  sortForms(data: any): void {
    this.forms = [];
    data.forEach((value: any) => {
      if (value.evaluatingFacultyMember === this.userName) {
        this.forms.push(value);
      }
    });

  }

  openReports(form2: any) {
    this.reportsLoading = true;
    this.reportsPage = true;
    this.reportFormId = form2.id;
    this.reportService.getReports(form2.id).subscribe({
      next: (data) => {
        this.reports = data
        this.reportsLoading = false;
        console.log(this.reports);
      },
      error: (err) => {
        console.error('Error fetching reports', err);
        this.reportsLoading = false;
      }
    });
  }

  closeReports(){
    this.reports =null;
    this.reportsPage = false;
    this.reportFormId =0;
    this.fetchReports();
  }

  downloadReport(base64Data: string, fileName: string) {
    console.log("Received base64Data:", base64Data);
    // Ensure Base64 is properly formatted
    let fixedBase64 = base64Data.replace(/_/g, '/').replace(/-/g, '+');
    while (fixedBase64.length % 4 !== 0) {
      fixedBase64 += '=';
    }
    console.log("Received base64Data:", base64Data);

    try {
      const byteCharacters = atob(fixedBase64);
      const byteNumbers = new Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const byteArray = new Uint8Array(byteNumbers);
      const fileBlob = new Blob([byteArray], { type: 'application/pdf' });

      // Create a download link
      const link = document.createElement('a');
      link.href = URL.createObjectURL(fileBlob);
      link.download = fileName;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (error) {
      console.error("Invalid Base64 data:", error);
    }
  }

  openCompanyEvaluation(id:number){
      this.showCompanyEvaluation = true;
  }

  openGrading(report:any){
    this.showGrading = true;
    this.selectedReport = report;
  }

  feedbackText: string = ''; // Default feedback text

  clearFeedback() {
    if (this.selectedOption === '1') {
      this.feedbackText = ''; // Clears textarea when "Accepted" is selected
    }
  }

  returnToReports(){
    this.showCompanyEvaluation = false;
    this.showGrading = false;
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
}
