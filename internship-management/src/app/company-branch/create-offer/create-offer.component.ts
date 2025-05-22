import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InternshipsService } from '../../../services/internships.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-create-offer',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './create-offer.component.html',
  styleUrls: ['./create-offer.component.css']
})
export class CreateOfferComponent {
  offerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private offerService: InternshipsService,
    private router: Router
  ) {
    this.offerForm = this.fb.group({
      position: ['', Validators.required],
      department: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      details: ['', Validators.required],
      description: ['']
    });
  }

  onSubmit() {
    if (this.offerForm.valid) {
      const companyUserName = localStorage.getItem('username');

      if (!companyUserName) {
        alert('Username not found. Please log in again.');
        return;
      }

      const formData = {
        ...this.offerForm.value,
        companyUserName: companyUserName
      };

      this.offerService.createInternshipOffer(formData).subscribe(() => {
        Swal.fire('Success', 'Internship offer created successfully!', 'success').then(() => {
          this.offerForm.reset();
          this.router.navigate(['/company-branch']);
        });
      });
    }
  }
}
