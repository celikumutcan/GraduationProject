import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ApprovedInternshipService, ApprovedInternship } from '../../../services/approved-internship.service';

@Component({
  selector: 'app-approved-internships',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './approved-internships.component.html',
  styleUrls: ['./approved-internships.component.css']
})
export class ApprovedInternshipsComponent implements OnInit {
  approvedInternships: ApprovedInternship[] = [];
  loading = false;

  constructor(
    private approvedInternshipService: ApprovedInternshipService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.fetchApprovedInternships();
  }

  fetchApprovedInternships(): void {
    this.loading = true;
    this.approvedInternshipService.getApprovedInternships().subscribe({
      next: (data) => {
        console.log("✅ Approved Internships received:", JSON.stringify(data, null, 2));

        // Veriyi doğru formatta işleyerek eksik değerleri varsayılan hale getir
        this.approvedInternships = data.map(internship => ({
          ...internship,
          internshipStartDate: internship.internshipStartDate || "N/A",
          internshipEndDate: internship.internshipEndDate || "N/A",
          companyUserName: internship.companyUserName || "Unknown",
          companyAddress: internship.companyAddress || "Unknown"
        }));

        console.log("Final Approved Internships Data:", this.approvedInternships);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("❌ Error fetching approved internships:", err);
        this.loading = false;
      }
    });
  }

  approveInsurance(internshipId: number): void {
    this.approvedInternshipService.approveInsurance(internshipId).subscribe({
      next: (response) => {
        console.log('✅ Insurance approved:', response);
        this.fetchApprovedInternships();
      },
      error: (err) => console.error('❌ Error approving insurance:', err)
    });
  }

  exportToExcel(): void {
    this.approvedInternshipService.exportToExcel().subscribe({
      next: (blob) => {
        console.log('📂 Exporting to Excel...');
        const a = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);
        a.href = objectUrl;
        a.download = 'approved_internships.xlsx';
        a.click();
        URL.revokeObjectURL(objectUrl);
      },
      error: (err) => console.error('❌ Error exporting to Excel:', err)
    });
  }
}
