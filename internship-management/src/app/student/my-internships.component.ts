import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DarkModeService } from '../../services/dark-mode.service';

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

  constructor(public darkModeService: DarkModeService) {}

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
    if (fileInput) {
      fileInput.click();
    }
  }

  // Handle upload action
  onUpload(): void {
    if (this.uploadedFile) {
      alert(`${this.uploadedFile.name} uploaded successfully.`);
      this.uploadedFile = null;
    } else {
      alert('Please select a file before uploading.');
    }
  }
}
