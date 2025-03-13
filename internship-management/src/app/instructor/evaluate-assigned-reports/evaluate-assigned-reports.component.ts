import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { EvaluateReportsService } from '../../../services/evaluate-reports.service'; // Servis yolunu projenize göre ayarlayın

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
  imports: [CommonModule, RouterModule],
  templateUrl: './evaluate-assigned-reports.component.html',
  styleUrls: ['./evaluate-assigned-reports.component.css']
})

export class EvaluateAssignedReportsComponent implements OnInit {
  reports: Report[] = [];
  loading: boolean = false;

  constructor(private evaluateReportsService: EvaluateReportsService) {}

  ngOnInit(): void {
    this.fetchReports();
  }

  fetchReports(): void {
    this.loading = true;
    this.evaluateReportsService.getAssignedReports().subscribe({
      next: (data: Report[]) => {
        this.reports = data.reverse();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching reports', err);
        this.loading = false;
      }
    });
  }

  viewReportDetails(report: Report): void {
    // Rapor detaylarını görüntülemek için modal açma veya yönlendirme işlemleri ekleyebilirsiniz.
    console.log('View details for', report);
  }
}
