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
  studentApplications: InternshipApplication[] = [];

  constructor(
    private http: HttpClient,
    private userService: UserService,
    private intershipService: InternshipsService,
    private cdr: ChangeDetectorRef
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

        this.intershipService.getInternshipApplicationsDTO(this.currentUser.userName).subscribe({
          next: (response: InternshipApplication[]) => {
            this.studentApplications = response;

            this.internships.forEach((internship) => {
              const pos2 = (internship.position || '').trim().toLowerCase();

              const isApplied = this.studentApplications.some((application) => {
                const pos1 = (application.position || '').trim().toLowerCase();
                const branchMatch = application.branchName.trim().toLowerCase() === internship.branchName.trim().toLowerCase();
                const positionMatch = pos1.includes(pos2) || pos2.includes(pos1);
                return branchMatch && positionMatch;
              });

              internship.applied = isApplied;
            });

            this.filteredInternships = [...this.internships];
            this.uniquePositions = this.getUniqueValues('position');
            this.uniqueCities = this.getUniqueValues('city');
            this.uniqueCountries = this.getUniqueValues('country');

            this.isLoading = false;
            this.cdr.detectChanges();
          },
          error: (err2: any) => {
            console.error('Error fetching applications DTO', err2);
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
        next: () => {
          const index = this.internships.findIndex(i => i.id === internship.id);
          if (index !== -1) {
            this.internships[index] = { ...this.internships[index], applied: true };
            this.filteredInternships = [...this.internships];
            this.cdr.detectChanges();
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
          this.cdr.detectChanges();
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
