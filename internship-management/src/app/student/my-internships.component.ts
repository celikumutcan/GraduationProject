import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DarkModeService } from '../../services/dark-mode.service';
import { DeadlineService } from '../../services/deadline.service';

@Component({
  selector: 'app-my-internships',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-internships.component.html',
  styleUrls: ['./my-internships.component.css'],
})
export class MyInternshipsComponent {
  internships = [
    { title: 'CNG 300 - Software Engineering', description: 'Developed a software project at TUBITAK.' },
    { title: 'CNG 400 - Professional Training', description: 'Worked in a professional environment at NVIDIA.' },
  ];

  selectedInternship: { title: string; description: string } | null = null;
  uploadedFile: File | null = null;

  today: string = new Date().toISOString().split('T')[0];
  currentReportDeadline: string = '';
  reportDeadlinePassed: boolean = false;

  constructor(
    public darkModeService: DarkModeService,
    private deadlineService: DeadlineService
  ) {
    this.getReportDeadline();
  }

  getReportDeadline(): void {
    this.deadlineService.getReportDeadline().subscribe({
      next: (res: any) => {
        this.currentReportDeadline = res.reportDeadline;
        this.reportDeadlinePassed = this.today > this.currentReportDeadline;
      },
      error: (err: any) => console.error('Error fetching report deadline', err)
    });
  }
  

  // Select an internship
  selectInternship(internship: { title: string; description: string }): void {
    this.selectedInternship = internship;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files && input.files.length > 0) {
      const file = input.files[0];
      if (file.type === 'application/pdf') {
        this.uploadedFile = file;
        alert(`${this.uploadedFile.name} file selected successfully.`);
      } else {
        alert('Only PDF files are allowed.');
        input.value = ''; // Clear the file input
      }
    }
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    event.stopPropagation();
    if (event.dataTransfer?.files.length) {
      const file = event.dataTransfer.files[0];
      if (file.type === 'application/pdf') {
        this.uploadedFile = file;
        alert(`${this.uploadedFile.name} file dropped successfully.`);
      } else {
        alert('Only PDF files are allowed.');
      }
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
  }

  // Trigger file input click programmatically
  triggerFileInput(): void {
    const fileInput = document.getElementById('upload') as HTMLInputElement;
    if (fileInput && !this.reportDeadlinePassed) {
      fileInput.click();
    }
  }

  // Handle upload action
  onUpload(): void {
    if (this.reportDeadlinePassed) {
      alert('The report deadline has passed. Upload is disabled.');
      return;
    }

    if (this.uploadedFile) {
      alert(`${this.uploadedFile.name} uploaded successfully.`);
      this.uploadedFile = null;
    } else {
      alert('Please select a file before uploading.');
    }
  }
}