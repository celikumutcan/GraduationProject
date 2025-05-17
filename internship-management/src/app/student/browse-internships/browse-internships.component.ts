import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
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
  internships: BrowseApprovedInternships[] = [];
  filteredInternships: BrowseApprovedInternships[] = [];
  isLoading = true;

  successMessage: string | null = null;
  currentUser: any;
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

  searchQuery: string = '';
  isSuccessVisible = false;
  positions: InternshipApplication[] = [];

  constructor(
    private http: HttpClient,
    private userService: UserService,
    private intershipService: InternshipsService,
    private cdr: ChangeDetectorRef // ✅ DOM'u manuel güncellemek için
  ) {}

  ngOnInit(): void {
    this.currentUser = this.userService.getUser();
    this.fetchInternships();
  }

  openDetails(internship: any) {
    this.selectedInternship = internship;
  }

  fetchInternships(): void {
    this.intershipService.getApprovedInternships().subscribe({
      next: (data) => {
        this.internships = data.reverse();

        this.intershipService.getInternshipApplications(this.currentUser.userName).subscribe({
          next: (response: InternshipApplication[]) => {
            this.positions = response;

            this.internships.forEach((internship) => {
              const internId = Number(internship.id);
              const isApplied = this.positions.some((position) => {
                const branchId = Number(position.branchId);
                const pos1 = (position.position || '').trim().toLowerCase();
                const pos2 = (internship.position || '').trim().toLowerCase();
                return branchId === internId && pos1 === pos2;
              });
              internship.applied = isApplied;
            });

            this.filteredInternships = [...this.internships];
            this.uniquePositions = this.getUniqueValues('position');
            this.uniqueCities = this.getUniqueValues('city');
            this.uniqueCountries = this.getUniqueValues('country');

            console.log('✅ FINAL INTERNSHIP LIST:', this.internships);
            console.log('✅ FINAL FILTERED LIST:', this.filteredInternships);
            console.log('✅ FINAL getFilteredInternships():', this.getFilteredInternships());

            this.isLoading = false;
            this.cdr.detectChanges(); // ✅ DOM'u manuel güncelle
          },
          error: (err2: any) => {
            console.error('Error fetching applications', err2);
            this.isLoading = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: (err) => {
        console.error('Error fetching internships', err);
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  getUniqueValues(key: keyof BrowseApprovedInternships): string[] {
    const values = this.internships.map((internship) => internship[key] as string);
    return [...new Set(values)];
  }

  getFilteredInternships(): BrowseApprovedInternships[] {
    return this.filteredInternships.filter((internship) => {
      const matchesFilters =
        (!this.filters.position || internship.position === this.filters.position) &&
        (!this.filters.branchName || internship.branchName === this.filters.branchName) &&
        (!this.filters.city || internship.city === this.filters.city) &&
        (!this.filters.country || internship.country === this.filters.country);

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

          const index = this.internships.findIndex(i => i.id === internship.id);
          if (index !== -1) {
            this.internships[index] = { ...this.internships[index], applied: true };
            this.filteredInternships = [...this.internships];
            this.cdr.detectChanges(); // ✅ Başvuru sonrası DOM'u güncelle
          }

          this.successMessage = '✅ Your application mail has been sent!';
          setTimeout(() => this.successMessage = null, 3000);
        },
        error: (err) => {
          console.error('Apply error:', err);
          this.successMessage = '❌ An error occurred. Please try again later.';
          this.cdr.detectChanges();
        },
        complete: () => {
          this.isSuccessVisible = true;
          document.body.style.cursor = 'default';
          document.documentElement.style.cursor = 'default';
          this.cdr.detectChanges(); // ✅ Popup görünümünü de tetikle
        }
      });
    }
  }

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
