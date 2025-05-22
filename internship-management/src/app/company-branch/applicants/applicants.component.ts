import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {CompanyService} from '../../../services/company.service';
import {InternshipApplication, InternshipsService} from '../../../services/internships.service';
import {Input} from '@angular/core';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-applicants',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './applicants.component.html',
  styleUrls: ['./applicants.component.css']
})
export class ApplicantsComponent implements OnInit{
  applications: any[] = [];
  userName: string = '';
  branchId: number =0;
  toastMessage: string | null = null;

  constructor(private companyService: CompanyService,
  private userService: UserService,
              private applicationService: InternshipsService
) {}

  ngOnInit(): void {
    this.userName = this.userService.getUser().userName;

    this.fetchBranchId();

  }

  fetchBranchId(): void {
    if (!this.userName) return;

    this.companyService.getBranchIdByUsername(this.userName).subscribe({
      next: (id) =>  {this.branchId = id;
        this.companyService.getCompanyApplications(this.branchId).subscribe({
          next: (data) => {this.applications = data;
            console.log(this.applications);},
          error: (err) => console.error('Error fetching applications', err),
        });
      },
      error: (err) => {
        console.error('Error fetching branch ID', err);
      },
    });
  }

  onApprove(applicationId: number): void {
    this.applicationService.approveInternshipApplication(applicationId).subscribe({
      next: (response) => {
        this.showToast('You approved an internship application.');
        this.updateStatus(applicationId, 'Accepted');
      },
      error: (err) => alert('Error approving application: ' + err.message),
    });
  }

  onReject(applicationId: number): void {
    this.applicationService.rejectInternshipApplication(applicationId).subscribe({
      next: (response) => {
        this.showToast('You rejected an internship application.');
        this.updateStatus(applicationId, 'Rejected');
      },
      error: (err) => alert('Error rejecting application: ' + err.message),
    });
  }

  showToast(message: string): void {
    this.toastMessage = message;
    setTimeout(() => (this.toastMessage = null), 3000);
  }

  updateStatus(applicationId: number, status: string): void {
    const app = this.applications.find(app => app.applicationId === applicationId);
    if (app) app.status = status;
  }

  downloadCV(username: string): void {
    window.open(`http://localhost:8080/api/resumes/download-cv/${username}`, '_blank');
  }
}
