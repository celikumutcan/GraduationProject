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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private internshipService: InternshipsService
  ) {}

  ngOnInit(): void {
    // ✅ İlk olarak URL parametresinden offerId'yi al
    this.route.params.subscribe(params => {
      this.offerId = +params['offerId'];

      // ✅ Ardından router state'ten pozisyonu al (başlık için)
      const nav = window.history.state;
      if (nav && nav['position']) {
        this.offerPosition = nav['position'];
      }

      // ✅ Şimdi başvuruları yükle
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
    console.log('Approving application ID:', applicationId); // 🪵 burada undefined mi kontrol et
    this.internshipService.approveInternshipApplication(applicationId).subscribe(() => {
      this.loadApplicants();
    });
  }


  rejectApplication(applicationId: number): void {
    this.internshipService.rejectInternshipApplication(applicationId).subscribe(() => {
      this.loadApplicants();
    });
  }
}
