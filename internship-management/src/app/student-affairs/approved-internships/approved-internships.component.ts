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
  approvedBy = "s_affairs"; // Student Affairs officer username

  constructor(
    private approvedInternshipService: ApprovedInternshipService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.fetchApprovedInternships();
  }

  // Fetch approved internships from the backend
  fetchApprovedInternships(): void {
    this.loading = true;
    this.approvedInternshipService.getApprovedInternships().subscribe({
      next: (data) => {
        console.log("✅ Approved Internships received:", JSON.stringify(data, null, 2));

        // Ensure missing values are replaced with meaningful defaults
        this.approvedInternships = data.map(internship => ({
          ...internship,
          name: internship.name || "Unknown", // Ensure student name is set
          lastName: internship.lastName || "Unknown", // Ensure student last name is set
          companyUserName: internship.companyUserName || "N/A", // Ensure company name is set
          companyAddress: internship.companyAddress || "N/A", // Ensure company address is set
          internshipStartDate: internship.internshipStartDate || "N/A",
          internshipEndDate: internship.internshipEndDate || "N/A"
        }));

        console.log("🔍 Final Approved Internships Data:", this.approvedInternships);
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error("❌ Error fetching approved internships:", err);
        this.loading = false;
      }
    });
  }

  // Approve insurance for a given internship ID
  approveInsurance(internshipId: number): void {
    this.approvedInternshipService.approveInsurance(internshipId, this.approvedBy).subscribe({
      next: (response) => {
        console.log('✅ Insurance approved:', response);
        this.approvedInternships=[];
        this.fetchApprovedInternships(); // Refresh the list after approval
      },
      error: (err) => {
        console.error('❌ Error approving insurance:', err);
        this.approvedInternships=[];
        this.fetchApprovedInternships(); },

      complete: ()=> {
      this.approvedInternships=[];
      this.fetchApprovedInternships(); // Refresh the list after approval
    }
    });
  }

  // Export internship data to an Excel file
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
