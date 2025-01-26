import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-browse-internships',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="p-8 relative">
      <h2 class="text-2xl font-bold mb-6">Browse Internships</h2>
      <div
        *ngFor="let internship of internships"
        class="flex items-center justify-between mb-6 bg-white text-gray-800 p-6 rounded-lg shadow-md hover:shadow-lg transition transform hover:-translate-y-1 dark:bg-gray-800 dark:text-white"
      >
        <div>
          <h3 class="text-xl font-semibold">{{ internship.company }}</h3>
          <p class="mt-2"><strong>Position:</strong> {{ internship.position }}</p>
          <p><strong>Duration:</strong> {{ internship.duration }}</p>
          <p class="mt-2">{{ internship.description }}</p>
        </div>
        <div class="flex items-center gap-4">
          <button
            class="px-4 py-2 rounded-lg text-white font-bold"
            [ngClass]="internship.applied ? 'bg-green-600' : 'bg-red-600 hover:bg-red-700'"
            (click)="applyToInternship(internship)"
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
export class BrowseInternshipsComponent {
  internships = [
    {
      company: 'Google',
      position: 'Software Engineer Intern',
      duration: 'June 2023 - August 2023',
      description: 'Worked on scalable backend services for Google Cloud Platform.',
      applied: false,
    },
    {
      company: 'Microsoft',
      position: 'Data Science Intern',
      duration: 'May 2023 - July 2023',
      description: 'Developed machine learning models for Bing search ranking.',
      applied: false,
    },
    {
      company: 'Amazon',
      position: 'Frontend Developer Intern',
      duration: 'July 2023 - September 2023',
      description: 'Enhanced user experience on Amazon Prime Video platform.',
      applied: false,
    },
  ];

  successMessage: string | null = null;

  applyToInternship(internship: any): void {
    if (!internship.applied) {
      internship.applied = true;
      this.successMessage = 'The company has been notified via e-mail.';
    }
  }

  closeMessage(): void {
    this.successMessage = null;
  }
}
