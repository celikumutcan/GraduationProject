import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { InternshipsService } from '../../../services/internships.service';

@Component({
  selector: 'app-offer-applicants',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './offer-applicants.component.html',
  styleUrls: ['./offer-applicants.component.css']
})
export class OfferApplicantsComponent {
  offerId: number = 0;
  offerPosition: string = '';
  applicants: any[] = [];
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private internshipService: InternshipsService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.offerId = +params['offerId'];

      const nav = window.history.state;
      if (nav && nav['position']) {
        this.offerPosition = nav['position'];
      }

      this.loadApplicants();
    });
  }

  loadApplicants(): void {
    this.internshipService.getOfferApplicants(this.offerId).subscribe((data) => {
      this.applicants = data;
    });
  }

  downloadCV(username: string): void {
    window.open(`http://localhost:8080/api/resumes/download-cv/${username}`, '_blank');
  }

  approveApplication(applicationId: number): void {
    this.internshipService.approveInternshipApplication(applicationId).subscribe({
      next: () => {
        this.successMessage = 'Application approved successfully.';
        this.errorMessage = '';
        this.loadApplicants();
        this.clearMessagesAfterDelay();
      },
      error: () => {
        this.errorMessage = 'Failed to approve application.';
        this.successMessage = '';
      }
    });
  }

  rejectApplication(applicationId: number): void {
    this.internshipService.rejectInternshipApplication(applicationId).subscribe({
      next: () => {
        this.successMessage = 'Application rejected successfully.';
        this.errorMessage = '';
        this.loadApplicants();
        this.clearMessagesAfterDelay();
      },
      error: () => {
        this.errorMessage = 'Failed to reject application.';
        this.successMessage = '';
      }
    });
  }

  clearMessagesAfterDelay(): void {
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 3000);
  }
}
