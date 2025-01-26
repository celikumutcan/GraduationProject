import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-my-resume',
  standalone: true,
  imports: [CommonModule], // CommonModule eklendi
  templateUrl: './my-resume.component.html',
  styleUrls: ['./my-resume.component.css'],
})
export class MyResumeComponent {
  resumeUploaded = false;
  uploadedFileName: string | null = null;

  // Dosya seçme işlemi
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files && input.files.length > 0) {
      const file = input.files[0];
      if (file.type === 'application/pdf') {
        this.uploadedFileName = file.name;
        this.resumeUploaded = true;
        alert('Resume uploaded successfully!');
      } else {
        alert('Only PDF files are allowed!');
      }
    }
  }

  // Dosya silme işlemi
  deleteResume(): void {
    this.uploadedFileName = null;
    this.resumeUploaded = false;
  }

  // Edit işlemi
  editResume(): void {
    this.resumeUploaded = false; // Dosya yükleme alanını göster
  }

  // Input tetikleme
  triggerFileInput(): void {
    const fileInput = document.getElementById('resumeUpload') as HTMLInputElement;
    if (fileInput) fileInput.click();
  }

  // Sürükleme işlemleri
  onDrop(event: DragEvent): void {
    event.preventDefault();
    const file = event.dataTransfer?.files[0];
    if (file && file.type === 'application/pdf') {
      this.uploadedFileName = file.name;
      this.resumeUploaded = true;
      alert('Resume uploaded successfully!');
    } else {
      alert('Only PDF files are allowed!');
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
  }
}
