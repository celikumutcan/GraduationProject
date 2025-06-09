import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgFor } from '@angular/common';
import { InternshipsService } from '../../../services/internships.service';
import Swal from 'sweetalert2';
import { forkJoin } from 'rxjs';

interface InternshipOfferListDTO {
  offerId: number;
  position: string;
  department: string;
  startDate: string;
  endDate: string;
  companyBranchName: string;
  details: string;
  description: string;
}

@Component({
  selector: 'app-open-internships',
  standalone: true,
  imports: [CommonModule, NgIf, NgFor],
  templateUrl: './open-internships.component.html',
  styleUrls: ['./open-internships.component.css']
})
export class OpenInternshipsComponent implements OnInit {
  internshipOffers: InternshipOfferListDTO[] = [];
  selectedOffer: InternshipOfferListDTO | null = null;
  isLoading = true;
  appliedOfferIds: number[] = [];

  constructor(private internshipService: InternshipsService) {}

  ngOnInit(): void {
    this.loadOffers();
  }

  loadOffers(): void {
    const studentUsername = localStorage.getItem('username');
    if (!studentUsername) {
      console.error('❌ Username not found in localStorage!');
      this.isLoading = false;
      return;
    }

    this.internshipService.getOpenInternshipOffers().subscribe((data) => {
      this.internshipOffers = data;

      const checks = this.internshipOffers.map((offer) =>
        this.internshipService.hasAppliedToOffer(studentUsername, offer.offerId)
      );

      forkJoin(checks).subscribe((results) => {
        results.forEach((hasApplied, index) => {
          if (hasApplied) {
            this.appliedOfferIds.push(this.internshipOffers[index].offerId);
          }
        });
        this.isLoading = false;
      });
    });
  }

  isOfferApplied(offerId: number): boolean {
    return this.appliedOfferIds.includes(offerId);
  }

  openDetails(offer: InternshipOfferListDTO): void {
    this.selectedOffer = offer;
  }

  closeDetails(): void {
    this.selectedOffer = null;
  }

  onImageError(event: any) {
    event.target.src = 'assets/Default-Logo.png';
  }


  selectedOfferApplied: boolean = false;

  applyToOffer(offerId: number): void {
    if (this.selectedOfferApplied) return;
    this.selectedOfferApplied = true;

    const studentUsername = localStorage.getItem('username');
    console.log('Clicked Apply!', studentUsername, offerId);

    if (!studentUsername) {
      console.error('❌ Username not found in localStorage!');
      Swal.fire('Error', 'User not logged in properly.', 'error');
      this.selectedOfferApplied = false;
      return;
    }

    this.internshipService.postApplyToInternshipOffer(studentUsername, offerId).subscribe({
      next: () => {
        if (!this.appliedOfferIds.includes(offerId)) {
          this.appliedOfferIds.push(offerId);
        }
        Swal.fire('Success', 'Application submitted successfully.', 'success').then(() => {
          this.selectedOffer = null;
          this.selectedOfferApplied = false;
        });
      },
      error: (err) => {
        console.error('❌ Apply error:', err);
        Swal.fire('Error', 'Application failed. You might have already applied.', 'error');
        this.selectedOfferApplied = false;
      }
    });
  }
}
