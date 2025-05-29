import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import { EvaluateReportsService, ReportEvaluationDTO } from '../../../services/evaluate-reports.service';
import {UserService} from '../../../services/user.service';
import {TraineeInformationFormService} from '../../../services/trainee-information-form.service';
import {ReportService,} from '../../../services/report.service';
import {FormsModule} from '@angular/forms';
import {HttpErrorResponse} from '@angular/common/http';
import {CompanyService} from '../../../services/company.service';
import {catchError, map, Observable, of} from 'rxjs'; // Servis yolunu projenize göre ayarlayın


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
    { name: 'Company Evaluation & Description', weight: 5, model: 'companyEvaluation', score: null ,comment: ''},
    { name: 'Report Structure', weight: 10, model: 'reportStructure', score: null ,comment: ''},
    { name: 'Abstract', weight: 5, model: 'abstract', score: null,comment: '' },
    { name: 'Problem Statement', weight: 5, model: 'problemStatement', score: null ,comment: '' },
    { name: 'Introduction', weight: 5, model: 'introduction', score: null ,comment: ''},
    { name: 'Theory', weight: 10, model: 'theory', score: null ,comment: ''},
    { name: 'Analyis', weight: 10, model: 'analysis', score: null ,comment: '' },
    { name: 'Modelling', weight: 15, model: 'modelling', score: null ,comment: ''},
    { name: 'Programming', weight: 20, model: 'programming', score: null ,comment: ''},
    { name: 'Testing', weight: 10, model: 'testing', score: null ,comment: ''},
    { name: 'Conclusion', weight: 5, model: 'conclusion', score: null ,comment: ''}
  ];
  comments: string = '';
  evaluatingform:any=null;
  attendance: string = '';
  diligence: string = '';
  contribution: string = '';
  performance: string = '';
  skipflag=false;

  totalGrade: number = 0;
  calculateTotal() {
    this.totalGrade = this.gradeItems.reduce((sum, item) => sum + (Number(item.score) || 0), 0);
  }

  increaseRows(item: { rows: number }) {
    item.rows!++;
  }

  showCompanyEvaluation=false;
  showGrading = false;
  selectedReport:any;

  feedbackText: string = '';

  selectedOption: string = '';

  evaluationStatusMap: { [key: number]: Observable<boolean> } = {};


  constructor(
    private evaluateReportsService: EvaluateReportsService,
    private userService: UserService,
    private router: Router,
    private companyService: CompanyService,
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
        this.forms.forEach(form => {
          this.evaluationStatusMap[form.id] = this.checkCompanyEvaluation(form.id);
        });


      },
      error: (err) => {
        console.error('Error fetching forms', err);
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  submitEvaluation(): void {
    console.log('Submit basıldı:', this.selectedOption, this.selectedReport);

    // --------------------------------------
    // 1) Eğer Accepted(S) seçiliyse:
    if (this.selectedOption === '1') {
      const dto: ReportEvaluationDTO = {
        feedback: this.feedbackText,
        reportId:           this.selectedReport.id,
        studentUserName: this.selectedReport.userName || this.selectedReport.traineeInformationForm?.fillUserName?.userName || '',
        companyEvalGrade:   this.gradeItems[0].score!,
        companyEvalComment: this.gradeItems[0].comment,
        reportStructureGrade:   this.gradeItems[1].score!,
        reportStructureComment: this.gradeItems[1].comment,
        abstractGrade:      this.gradeItems[2].score!,
        abstractComment:    this.gradeItems[2].comment,
        problemStatementGrade:   this.gradeItems[3].score!,
        problemStatementComment: this.gradeItems[3].comment,
        introductionGrade:  this.gradeItems[4].score!,
        introductionComment:this.gradeItems[4].comment,
        theoryGrade:        this.gradeItems[5].score!,
        theoryComment:      this.gradeItems[5].comment,
        analysisGrade:      this.gradeItems[6].score!,
        analysisComment:    this.gradeItems[6].comment,
        modellingGrade:     this.gradeItems[7].score!,
        modellingComment:   this.gradeItems[7].comment,
        programmingGrade:   this.gradeItems[8].score!,
        programmingComment: this.gradeItems[8].comment,
        testingGrade:       this.gradeItems[9].score!,
        testingComment:     this.gradeItems[9].comment,
        conclusionGrade:    this.gradeItems[10].score!,
        conclusionComment:  this.gradeItems[10].comment,
      };



      this.evaluateReportsService.createAllEvaluations(dto).subscribe({
        next: () => {
          // backend zaten status ve grade atıyor
          this.selectedReport.status = 'Graded';
          this.selectedReport.grade  = this.totalGrade > 60 ? 'S' : 'U';
          this.selectedReport.feedback = this.feedbackText; // ⬅ BUNU EKLE
          this.returnToReports();
          console.log("👀 selectedReport:", this.selectedReport);
          console.log("✅ userName:", this.selectedReport?.userName);
          console.log("✅ traineeInfo userName:", this.selectedReport?.traineeInformationForm?.fillUserName?.userName);
        },
        error: (err: HttpErrorResponse) => {
          console.error('Evaluation save error', err);
          alert('Değerlendirme kaydedilemedi.');
        }
      });
      return;
    }

    // --------------------------------------
    // 2) Eğer Rejected (U) seçiliyse:
    if (this.selectedOption === '2') {
      this.evaluateReportsService
        .rejectReport(this.selectedReport.id, this.feedbackText)
        .subscribe({
          next: () => {
            this.selectedReport.status   = 'Rejected';
            this.selectedReport.feedback = this.feedbackText;
            this.returnToReports();
          },
          error: (err: HttpErrorResponse) => {
            console.error('Reject error', err);
            alert('Rapor reddedilemedi.');
          }
        });
      return;
    }

    // --------------------------------------
    // 3) Eğer Needs to be corrected seçiliyse:
    if (this.selectedOption === '3') {
      this.evaluateReportsService
        .correctionReport(this.selectedReport.id, this.feedbackText)
        .subscribe({
          next: () => {
            this.selectedReport.status   = 'Instructor Feedback Waiting';
            this.selectedReport.feedback = this.feedbackText;
            this.returnToReports();
          },
          error: (err: HttpErrorResponse) => {
            console.error('Correction error', err);
            alert('Düzeltme isteği gönderilemedi.');
          }
        });
      return;
    }
  }


  /** “Rejected” butonuna bağlanacak */
  onReject(): void {
    const reason = prompt('Enter rejection reason:');
    if (!reason) return;
    this.evaluateReportsService.rejectReport(this.selectedReport.id, reason).subscribe({
      next: () => {
        alert('Report rejected.');
        this.closeGradingModal();
        this.fetchReports();
      },
      error: err => {
        console.error(err);
        alert('Reject failed.');
      }
    });
  }

  /** “Needs to be corrected” butonuna bağlanacak */
  onCorrection(): void {
    const reason = prompt('Enter correction reason:');
    if (!reason) return;
    this.evaluateReportsService.correctionReport(this.selectedReport.id, reason).subscribe({
      next: () => {
        alert('Marked for correction.');
        this.closeGradingModal();
        this.fetchReports();
      },
      error: err => {
        console.error(err);
        alert('Operation failed.');
      }
    });
  }

  private closeGradingModal() {
    this.showGrading = false;
    this.selectedReport = null;
    this.gradeItems.forEach(i => {
      i.score = null; i.comment = '';
    });
    this.totalGrade = 0;
  }

  /** Clamp a single item's score between 0 and its weight, then recompute */
  onScoreChange(item: { name: string; weight: number; model: string; score: null; comment: string } | {
    name: string;
    weight: number;
    model: string;
    score: null;
    comment: string
  } | { name: string; weight: number; model: string; score: null; comment: string } | {
    name: string;
    weight: number;
    model: string;
    score: null;
    comment: string
  } | { name: string; weight: number; model: string; score: null; comment: string } | {
    name: string;
    weight: number;
    model: string;
    score: null;
    comment: string
  } | { name: string; weight: number; model: string; score: null; comment: string } | {
    name: string;
    weight: number;
    model: string;
    score: null;
    comment: string
  } | { name: string; weight: number; model: string; score: null; comment: string } | {
    name: string;
    weight: number;
    model: string;
    score: null;
    comment: string
  } | { name: string; weight: number; model: string; score: null; comment: string }) {
    if (item.score == null || item.score < 0) {
      // @ts-ignore
      item.score = 0;
    } else if (item.score > item.weight) {
      // @ts-ignore
      item.score = item.weight;
    }
    this.calculateTotal();
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

  openCompanyEval(form2:any){
    this.reportsPage = true;
    this.skipflag = true;
    this.reportFormId = form2.id;
    this.openCompanyEvaluation(this.reportFormId);
  }

  downloadEvaluationCsv(reportId: number): void {
    this.evaluateReportsService.getEvaluations(reportId).subscribe({
      next: (evaluations) => {
        const headers = ['Item', 'Score', 'Weight', 'Comment'];
        const rows = evaluations.map((e: any) => [
          e.itemName,
          e.score,
          e.weight,
          e.comment
        ]);

        const feedbackRow = ['Instructor Feedback', '', '', this.selectedReport?.feedback || ''];
        rows.push(feedbackRow);

        const csvContent = [headers, ...rows]
          .map(row => row.map(cell => `"${cell}"`).join(';'))
          .join('\r\n');

        const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' });

        this.evaluateReportsService.getStudentUserName(reportId).subscribe({
          next: (username: string) => {
            const a = document.createElement('a');
            a.href = URL.createObjectURL(blob);
            a.download = `report_evaluation_${username}.csv`;
            a.click();
            URL.revokeObjectURL(a.href);
          },
          error: () => {
            const a = document.createElement('a');
            a.href = URL.createObjectURL(blob);
            a.download = `report_evaluation_unknown.csv`;
            a.click();
            URL.revokeObjectURL(a.href);
          }
        });
      },
      error: err => {
        console.error('CSV download error', err);
        alert('CSV indirilemedi.');
      }
    });
  }



  closeReports(){
    this.reports =null;
    this.reportsPage = false;
    this.reportFormId =0;
    this.fetchReports();
    this.comments = '';
    this.evaluatingform=null;
    this.attendance= '';
    this.diligence= '';
    this.contribution = '';
    this.performance = '';
  }

  closeCompanyEvaluation(){
    this.reports =null;
    this.reportsPage = false;
    this.reportFormId =0;
    this.showCompanyEvaluation = false;
    this.skipflag = false;
    this.comments = '';
    this.evaluatingform=null;
    this.attendance= '';
    this.diligence= '';
    this.contribution = '';
    this.performance = '';
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

  preventChange(event: Event): void {
    event.preventDefault(); // Prevent the click from changing the value
  }


  openGrading(report: any) {
    this.showGrading = true;
    this.selectedReport = report;
    console.log('📦 selectedReport:', this.selectedReport);

    // Feedback'i doldur
    this.feedbackText = report.feedback || '';

    // Backend'den değerlendirme yorum ve puanlarını çek
    this.evaluateReportsService.getEvaluations(report.id).subscribe((evaluations) => {
      this.gradeItems.forEach((item) => {
        const matched = evaluations.find(e => e.itemName === item.name);
        if (matched) {
          item.score = matched.score;
          item.comment = matched.comment;
        }
      });

      // Toplam puanı da yeniden hesapla
      this.calculateTotal();
    });
  }

  autoResize(event: Event) {
    const ta = event.target as HTMLTextAreaElement;
    ta.style.height = 'auto';
    ta.style.height = ta.scrollHeight + 'px';
  }

  getCommentWidth(): string {
    // Mesela: rejected ise daha geniş, accepted ise sabit
    return this.selectedOption === '2' ? '400px' : '300px';
  }


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
