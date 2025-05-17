import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { UserService } from '../../../services/user.service';
import {
  BrowseApprovedInternships,
  InternshipsService,
  InternshipApplication
} from '../../../services/internships.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-browse-internships',
  standalone: true,
  imports: [CommonModule, FormsModule],
  providers: [UserService],
  templateUrl: './browse-internships.component.html',
  styleUrls: ['./browse-internships.component.css']
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

  // Arama çubuğu için kullanılacak değişken
  searchQuery: string = '';
  //Successful Application flag
  isSuccessVisible=false;

  constructor(
    private http: HttpClient,
    private userService: UserService,
    private intershipService: InternshipsService
  ) {}

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
      const matchesFilters =
        (!this.filters.position || internship.position === this.filters.position) &&
        (!this.filters.branchName || internship.branchName === this.filters.branchName) &&
        (!this.filters.city || internship.city === this.filters.city) &&
        (!this.filters.country || internship.country === this.filters.country);

      // Arama kutusundaki metin
      const query = this.searchQuery.toLowerCase();
      const matchesSearch =
        !this.searchQuery ||
        internship.branchName.toLowerCase().includes(query) ||
        internship.position.toLowerCase().includes(query) ||
        internship.city.toLowerCase().includes(query) ||
        internship.country.toLowerCase().includes(query);

      return matchesFilters && matchesSearch;
    });
  }

applyToInternship(internship: BrowseApprovedInternships): void {
  document.body.style.cursor = 'wait';
  document.documentElement.style.cursor = 'wait';

  if (!internship.applied) {
    this.intershipService.postApplyInternship(this.currentUser.userName, internship.id).subscribe({
      next: (response: any) => {
        document.body.style.cursor = 'default';

        // ✅ Direkt başarılıysa ne dönerse dönsün çalıştır
        const index = this.internships.findIndex(i => i.id === internship.id);
        if (index !== -1) {
          this.internships[index] = { ...this.internships[index], applied: true };
        }

        this.successMessage = '✅ Your application mail has been sent!';
        setTimeout(() => this.successMessage = null, 3000);
      },
      error: (err) => {

        console.error('Apply error:', err); // mutlaka bu kalmalı
        this.successMessage = '❌ An error occurred. Please try again later.';
      },
      complete: () => {
        this.isSuccessVisible = true;

        document.body.style.cursor = 'default';
        document.documentElement.style.cursor = 'default';

      }
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
  closeSuccess() {
    this.isSuccessVisible = false;
  }
}
