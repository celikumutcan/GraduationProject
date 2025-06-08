import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-create-offer',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './create-offer.component.html',
  styleUrls: ['./create-offer.component.css']
})
export class CreateOfferComponent implements OnInit {
  offerForm: FormGroup;
  selectedImage: File | null = null;
  loggedInUsername: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private http: HttpClient,
    private userService: UserService // kullanıcı adını buradan alıyoruz
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

  ngOnInit(): void {
    const currentUser = this.userService.getUser();
    this.loggedInUsername = currentUser?.userName || '';
  }

  onImageSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file && file.type.startsWith('image/')) {
      this.selectedImage = file;
      console.log('📷 Image selected:', file.name);
    } else {
      console.warn('❌ Invalid image file selected.');
    }
  }

  onSubmit(): void {
    if (this.offerForm.invalid || !this.loggedInUsername) {
      return;
    }

    const formData = new FormData();
    formData.append('position', this.offerForm.value.position);
    formData.append('department', this.offerForm.value.department);
    formData.append('details', this.offerForm.value.details);
    formData.append('startDate', this.offerForm.value.startDate);
    formData.append('endDate', this.offerForm.value.endDate);
    formData.append('description', this.offerForm.value.description || '');
    formData.append('companyUserName', this.loggedInUsername);

    if (this.selectedImage) {
      formData.append('image', this.selectedImage);
    }

    this.http.post('http://localhost:8080/api/internship-offers/create', formData).subscribe({
      next: () => {
        Swal.fire('Success!', 'Internship offer created.', 'success');
        this.router.navigate(['/company-branch/my-offers']);
      },
      error: () => {
        Swal.fire('Error', 'Failed to create internship offer.', 'error');
      }
    });
  }
}
