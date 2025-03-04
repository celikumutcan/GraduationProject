import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserService } from '../../../services/user.service';
import {
  BrowseApprovedInternships,
  InternshipsService,
  InternshipApplication
} from '../../../services/internships.service';
import {FormsModule} from '@angular/forms';



@Component({
  selector: 'app-browse-internships',
  standalone: true,
  imports: [CommonModule, FormsModule],
  providers: [UserService],
  template: `
    <div class="p-8 relative" [ngClass]="{ 'dark': isDarkMode }">
      <h2 class="text-2xl font-bold mb-6 text-white-800 dark:text-white">Browse Internships</h2>

      <!-- Filtreleme Alanı -->
      <div class="mb-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <!-- Pozisyon Filtresi -->
        <select
          [(ngModel)]="filters.position"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500 dark:bg-gray-700 dark:text-white dark:border-gray-600"
        >
          <option value="">All Positions</option>
          <option *ngFor="let position of uniquePositions" [value]="position">{{ position }}</option>
        </select>

        <!-- Şehir Filtresi -->
        <select
          [(ngModel)]="filters.city"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500 dark:bg-gray-700 dark:text-white dark:border-gray-600"
        >
          <option value="">All Cities</option>
          <option *ngFor="let city of uniqueCities" [value]="city">{{ city }}</option>
        </select>

        <!-- Ülke Filtresi -->
        <select
          [(ngModel)]="filters.country"
          class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500 dark:bg-gray-700 dark:text-white dark:border-gray-600"
        >
          <option value="">All Countries</option>
          <option *ngFor="let country of uniqueCountries" [value]="country">{{ country }}</option>
        </select>

      </div>

      <!-- Tablo Şeklinde Gösterme -->
      <table class="w-full bg-white rounded-lg shadow-md dark:bg-gray-800">
        <thead>
        <tr class="bg-gray-100 dark:bg-gray-700">
          <th class="px-4 py-2 text-left text-gray-800 dark:text-white">Company Name</th>
          <th class="px-4 py-2 text-left text-gray-800 dark:text-white">Position</th>
          <th class="px-4 py-2 text-left text-gray-800 dark:text-white">Location</th>
          <th class="px-4 py-2 text-left text-gray-800 dark:text-white">Date</th>
          <th class="px-4 py-2 text-left text-gray-800 dark:text-white">Action</th>
        </tr>
        </thead>
        <tbody>
        <tr
          *ngFor="let internship of filteredInternships"
          class="border-b border-gray-200 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-700"
        >
          <td class="px-4 py-2 text-gray-800 dark:text-white">{{ internship.branchName }}</td>
          <td class="px-4 py-2 text-gray-800 dark:text-white">{{ internship.position }}</td>
          <td class="px-4 py-2 text-gray-800 dark:text-white">{{ internship.city }}, {{ internship.country }}</td>
          <td class="px-4 py-2 text-gray-800 dark:text-white">{{ internship.datetime | date }}</td>
          <td class="px-4 py-2 d-flex space-x-2 gap-5">
            <button
              class="px-4 py-2 rounded-lg text-white font-bold"
              [ngClass]="internship.applied ? 'bg-green-600' : 'bg-red-600 hover:bg-red-700'"
              (click)="applyToInternship(internship)"
              [disabled]="internship.applied"
            >
              {{ internship.applied ? 'Applied' : 'Apply' }}
            </button>
            <button
              class="px-4 py-2 ml-14 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-bold"
              (click)="openDetails(internship)"
            >
              Details
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Details -->
    <!-- Modal -->
    <div
      *ngIf="selectedInternship"
      class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50"
    >
      <div class="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-lg max-w-2xl w-full">
        <h2 class="text-2xl font-bold mb-4 text-gray-800 dark:text-white">
          Internship Details
        </h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 text-gray-700 dark:text-gray-300">
          <div><strong>Company Name:</strong> {{ selectedInternship.branchName }}</div>
          <div><strong>Position:</strong> {{ selectedInternship.position }}</div>
          <div><strong>Company Address:</strong> {{ selectedInternship.companyAddress }}</div>
          <div><strong>Company Phone:</strong> {{ selectedInternship.companyPhone }}</div>
          <div><strong>Company Email:</strong> {{ selectedInternship.companyEmail }}</div>
          <div><strong>Country:</strong> {{ selectedInternship.country }}</div>
          <div><strong>City:</strong> {{ selectedInternship.city }}</div>
          <div><strong>District:</strong> {{ selectedInternship.district }}</div>
          <div>
          <div><strong>Internship Participants:</strong></div>
          <div><strong>Name:</strong> {{ selectedInternship.name }} {{ selectedInternship.lastName }}</div>
          <div><strong>Mail:</strong> {{ selectedInternship.username.concat("\@metu.edu.tr")  }} </div>
          <div><strong>Date:</strong> {{ selectedInternship.datetime | date }}</div>
          </div>
          <div>
          <div><strong>Type:</strong> {{ selectedInternship.type }}</div>
          <div><strong>Code:</strong> {{ selectedInternship.code }}</div>
          </div>

        </div>
        <div class="mt-6 text-right">
          <button
            class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
            (click)="selectedInternship = null"
          >
            Close
          </button>
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
      /* Dropdown menü stilleri */
      select {
        color: black; /* Metin rengi siyah */
        background-color: white; /* Arka plan rengi beyaz */
      }
      /* Dark mode için dropdown menü stilleri */
      .dark select {
        color: white; /* Dark mode'da metin rengi beyaz */
        background-color: #2d3748; /* Dark mode'da arka plan rengi koyu gri */
      }
    `,
  ],
})


export class BrowseInternshipsComponent implements OnInit {
  internships: BrowseApprovedInternships[] = []; // Gerçek staj ilanları
  successMessage: string | null = null;
  currentUser: any; // Mevcut kullanıcı bilgileri
  uniquePositions: string[] = [];
  uniqueCities: string[] = [];
  uniqueCountries: string[] = [];
  uniqueSemesters: string[] = [];
  isDarkMode = false;
  selectedInternship: any = null;
  filters: any = {
    position: '',
    city: '',
    country: '',
    semester: '',
    branchName: '',

  };



  constructor(private http: HttpClient, private userService: UserService, private intershipService: InternshipsService) {}

  ngOnInit(): void {
    // Component yüklendiğinde kullanıcı bilgilerini al
    this.currentUser = this.userService.getUser();
    // Kullanıcı bilgileri alındıktan sonra staj ilanlarını çek
    this.fetchInternships();
  }


  openDetails(internship: any) {
    this.selectedInternship = internship;
  }

  positions: InternshipApplication[] = [];

  fetchInternships(): void {
    this.intershipService.getApprovedInternships().subscribe({
      next: (data) => {
        this.internships = data.reverse();

        this.intershipService.getInternshipApplications(this.currentUser.userName).subscribe({
          next: (response: InternshipApplication[]) => {
            this.positions = response;

            this.internships.forEach((internship) => {
              const isApplied = this.positions.some(
                (position) =>
                  position.branchId === internship.id &&
                  position.position === internship.position
              );
              internship.applied = isApplied;
            });

            this.uniquePositions = this.getUniqueValues('position');
            this.uniqueCities = this.getUniqueValues('city');
            this.uniqueCountries = this.getUniqueValues('country');
          },
          error: (err2: any) => {
            console.error('Error fetching approvals', err2);
          }
        });

      },
      error: (err) => {
        console.error('Error fetching internships', err);
      }
    });
  }


  getUniqueValues(key: keyof BrowseApprovedInternships): string[] {
    const values = this.internships.map((internship) => internship[key] as string);
    return [...new Set(values)];
  }

  get filteredInternships(): BrowseApprovedInternships[] {
    return this.internships.filter((internship) => {
      return (
        (!this.filters.position || internship.position === this.filters.position) &&
        (!this.filters.branchName || internship.branchName === this.filters.branchName) &&
        (!this.filters.city || internship.city === this.filters.city) &&
        (!this.filters.country || internship.country === this.filters.country)
      );
    });
  }


  // Staj ilanına başvuru yap
  applyToInternship(internship: BrowseApprovedInternships): void {
    if (!internship.applied) {

      this.intershipService.postApplyInternship(this.currentUser.userName, internship.id).subscribe({
        next: (response: string) => {
          if (response === "Internship application for the offer submitted successfully.") {
            internship.applied = true;
            this.successMessage = 'The company has been notified via e-mail.';
          } else {
            this.successMessage = 'Application failed. Please try again.';
          }
        },
        error: (err: any) => {
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

  toggleDarkMode() {
    this.isDarkMode = !this.isDarkMode;
    document.documentElement.classList.toggle('dark', this.isDarkMode);
  }

}
