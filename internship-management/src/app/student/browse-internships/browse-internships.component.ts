import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserService } from '../../../services/user.service';

export interface Internship {
  id: number;
  datetime: string;
  position: string;
  code: string;
  semester: string;
  country: string;
  city: string;
  applied?: boolean; // Başvuru durumunu takip etmek için ekledik
}

@Component({
  selector: 'app-browse-internships',
  standalone: true,
  imports: [CommonModule],
  providers: [UserService],
  template: `
    <div class="p-8 relative">
      <h2 class="text-2xl font-bold mb-6">Browse Internships</h2>
      <div
        *ngFor="let internship of internships"
        class="flex items-center justify-between mb-6 bg-white text-gray-800 p-6 rounded-lg shadow-md hover:shadow-lg transition transform hover:-translate-y-1 dark:bg-gray-800 dark:text-white"
      >
        <div>
          <h3 class="text-xl font-semibold">{{ internship.position }}</h3>
          <p class="mt-2"><strong>Code:</strong> {{ internship.code }}</p>
          <p><strong>Semester:</strong> {{ internship.semester }}</p>
          <p class="mt-2"><strong>Location:</strong> {{ internship.city }}, {{ internship.country }}</p>
          <p><strong>Date:</strong> {{ internship.datetime | date }}</p>
        </div>
        <div class="flex items-center gap-4">
          <button
            class="px-4 py-2 rounded-lg text-white font-bold"
            [ngClass]="internship.applied ? 'bg-green-600' : 'bg-red-600 hover:bg-red-700'"
            (click)="applyToInternship(internship)"
            [disabled]="internship.applied"
          >
            {{ internship.applied ? 'Applied' : 'Apply' }}
          </button>
          <span
            class="material-icons text-gray-500 cursor-pointer"
            title="Pressing the Apply button will send your informations to the Company."
          >
            info
          </span>
        </div>
      </div>
      <div
        *ngIf="successMessage"
        class="absolute inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50 z-50"
        (click)="closeMessage()"
      >
        <div class="bg-white text-gray-800 p-6 rounded-lg shadow-lg">
          <p class="text-lg font-bold text-center">{{ successMessage }}</p>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .p-8 {
        padding: 2rem;
      }
      .text-2xl {
        font-size: 1.5rem;
        font-weight: 700;
      }
      .mb-6 {
        margin-bottom: 1.5rem;
      }
      button {
        transition: background-color 0.3s ease-in-out;
      }
    `,
  ],
})
export class BrowseInternshipsComponent implements OnInit {
  internships: Internship[] = []; // Gerçek staj ilanları
  successMessage: string | null = null;
  currentUser: any; // Mevcut kullanıcı bilgileri

  constructor(private http: HttpClient, private userService: UserService) {}

  ngOnInit(): void {
    // Component yüklendiğinde kullanıcı bilgilerini al
    this.userService.getUser().subscribe({
      next: (user: any) => {
        this.currentUser = user;
        // Kullanıcı bilgileri alındıktan sonra staj ilanlarını çek
        this.fetchAllInternships();
      },
      error: (err: any) => {
        console.error('Error fetching user data:', err);
      },
    });
  }

  // Tüm staj ilanlarını API'den çek
  fetchAllInternships(): void {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token')}`, // JWT token
    });

    this.http.get<Internship[]>('http://localhost:8080/api/approved_trainee_information_form', { headers })
      .subscribe({
        next: (internships) => {
          this.internships = internships.map((internship) => ({
            ...internship,
            applied: false, // Varsayılan olarak başvuru yapılmamış olarak işaretle
          }));
        },
        error: (err) => {
          console.error('Error fetching internships:', err);
        },
      });
  }

  // Staj ilanına başvuru yap
  applyToInternship(internship: Internship): void {
    if (!internship.applied) {
      const applicationData = {
        student_user_name: this.currentUser.userName, // Öğrenci kullanıcı adı
        position: internship.position, // Pozisyon
        code: internship.code, // Kod
        semester: internship.semester, // Dönem
        country: internship.country, // Ülke
        city: internship.city, // Şehir
      };

      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`, // JWT token
      });

      this.http.post<any>('http://localhost:8080/api/internship-applications/applyForOffer', applicationData, { headers })
        .subscribe({
          next: (response) => {
            if (response.success) {
              internship.applied = true;
              this.successMessage = 'The company has been notified via e-mail.';
            } else {
              this.successMessage = 'Application failed. Please try again.';
            }
          },
          error: (err) => {
            console.error('Error applying to internship:', err);
            this.successMessage = 'An error occurred. Please try again later.';
          },
        });
    }
  }

  // Başarı mesajını kapat
  closeMessage(): void {
    this.successMessage = null;
  }
}