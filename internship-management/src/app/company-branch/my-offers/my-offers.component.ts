import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InternshipsService } from '../../../services/internships.service';

@Component({
  selector: 'app-my-offers',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-offers.component.html',
  styleUrls: ['./my-offers.component.css']
})
export class MyOffersComponent {
  internshipOffers: any[] = [];
  username: string | null = '';

  constructor(private offerService: InternshipsService, private router: Router) {}

  ngOnInit() {
    this.username = localStorage.getItem('username');
    if (this.username) {
      this.offerService.getCompanyInternshipOffers(this.username).subscribe((data) => {
        this.internshipOffers = data;
      });
    }
  }

  viewApplicants(offerId: number, position: string) {
    this.router.navigate(['/company-branch/offer-applicants', offerId], {
      state: { position: position }
    });
  }

  createNewOffer() {
    this.router.navigate(['/company-branch/create-offer']);
  }
}
