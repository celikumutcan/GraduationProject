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
  gradeItems = [
    { name: 'Company Evaluation & Description', weight: 5, model: 'companyEvaluation', score: null },
    { name: 'Report Structure', weight: 10, model: 'reportStructure', score: null },
    { name: 'Abstract', weight: 5, model: 'abstract', score: null },
    { name: 'Problem Statement', weight: 5, model: 'problemStatement', score: null },
    { name: 'Introduction', weight: 5, model: 'introduction', score: null },
    { name: 'Theory', weight: 10, model: 'theory', score: null },
    { name: 'Analyis', weight: 10, model: 'analysis', score: null },
    { name: 'Modelling', weight: 15, model: 'modelling', score: null },
    { name: 'Programming', weight: 20, model: 'programming', score: null },
    { name: 'Testing', weight: 10, model: 'testing', score: null },
    { name: 'Conclusion', weight: 5, model: 'conclusion', score: null }
  ];




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

  downloadReport(reportId: number) {
    this.reportService.downloadReport(reportId).subscribe({
      next: (fileData: Blob) => {
        const downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(fileData);
        downloadLink.download = `report_${reportId}.pdf`;
        downloadLink.click();
      },
      error: (err) => {
        console.error('Error downloading report:', err);
      }
    });
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



}
