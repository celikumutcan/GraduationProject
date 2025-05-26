import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ResumeService } from '../../services/resume.service';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common'; // ✅ EKLENDİ

@Component({
  selector: 'app-my-resume',
  standalone: true,
  templateUrl: './my-resume.component.html',
  styleUrls: ['./my-resume.component.css'],
  imports: [CommonModule, HttpClientModule], // ✅ BU SATIR ZORUNLU
})
export class MyResumeComponent implements OnInit {
  resumeUploaded = false;
  uploadedFileName: string | null = null;
  selectedFile!: File;
  username!: string;
  resumeId!: number;
  isLoading: boolean = true;


  constructor(private resumeService: ResumeService, private userService: UserService ,  private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    console.log('Local user loaded:', localStorage.getItem('currentUser'));

    const currentUser = this.userService.getUser();
    if (!currentUser || !currentUser.userName) {
      alert('Kullanıcı bilgisi alınamadı.');
      return;
    }
    this.username = currentUser.userName;
    this.loadResume();
  }
  loadResume(): void {
    this.isLoading = true;
    this.resumeService.getAllResumes().subscribe(resumes => {
      const found = resumes.find((r: any) => r.userName === this.username);
      if (found) {
        this.resumeUploaded = true;
        this.uploadedFileName = found.fileName;
        this.resumeId = found.id;
      } else {
        this.resumeUploaded = false;
        this.uploadedFileName = null;
        this.resumeId = 0;
      }
      this.isLoading = false;
    }, error => {
      console.error("CV çekme hatası:", error);
      this.isLoading = false;
    });
  }
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input?.files && input.files.length > 0) {
      const file = input.files[0];
      if (file.type === 'application/pdf') {
        this.selectedFile = file;
        this.uploadResume();
      } else {
        alert('Sadece PDF dosyası yükleyebilirsiniz.');
      }
    }
  }

  uploadResume(): void {
    if (this.resumeUploaded) {
      alert('Zaten bir CV yüklediniz.');
      return;
    }

    this.resumeService.uploadResume(this.username, this.selectedFile).subscribe({
      next: () => {
        this.resumeUploaded = true;
        this.uploadedFileName = this.selectedFile.name;
        this.selectedFile = undefined as any;
      },
      error: (err) => {
        console.error(err);
        if (
          err?.error?.message?.includes('Resume already exists') ||
          JSON.stringify(err)?.includes('already exists')
        ) {
          this.resumeUploaded = true;
          this.uploadedFileName = this.selectedFile.name;
          alert('Zaten bir özgeçmiş yüklediniz. Güncellemek için lütfen Edit butonunu kullanın.');
        } else {
          alert('CV yüklenemedi. Hata: ' + JSON.stringify(err));
        }
      }
    });
  }
  deleteResume(): void {
    console.log("Delete fonksiyonu tetiklendi."); // 👈 Bu console'a yazılmalı
    if (!this.resumeId) {
      console.log("ResumeId yok");
      return;
    }

    this.resumeService.deleteResume(this.resumeId).subscribe({
      next: () => {
        console.log("Başarıyla silindi");
        this.resumeUploaded = false;
        this.uploadedFileName = null;
        this.resumeId = 0;
      },
      error: (err) => {
        console.error("Silme hatası:", err);
      }
    });
  }
  downloadResume(): void {
    if (!this.resumeId) return;

    this.resumeService.downloadResume(this.username).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = this.uploadedFileName || 'resume.pdf';
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    }, error => {
      alert('Dosya indirilemedi.');
      console.error(error);
    });
  }

  triggerFileInput(): void {
    const fileInput = document.getElementById('resumeUpload') as HTMLInputElement;
    if (fileInput) fileInput.click();
  }
  editResume(): void {
    this.resumeUploaded = false;
    this.uploadedFileName = null;
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    const file = event.dataTransfer?.files[0];
    if (file && file.type === 'application/pdf') {
      this.selectedFile = file;
      this.uploadResume();
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
