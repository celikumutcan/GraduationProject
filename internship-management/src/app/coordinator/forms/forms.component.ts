import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {EvaluateReportsService} from '../../../services/evaluate-reports.service';
import {UserService} from '../../../services/user.service';
import {Router} from '@angular/router';
import {ReportService} from '../../../services/report.service';
import {FormService} from '../../../services/form.service';
import {HttpClient} from '@angular/common/http';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-forms',
  standalone: true,
  imports: [CommonModule], // CommonModule eklendi
  templateUrl: './forms.component.html',
  styleUrls: ['./forms.component.css'],
})
export class FormsComponent implements OnInit{
  userName = "";
  uploadError: string | null = null; // Hata mesajları için
  forms: any[] = [];

  constructor(
    private userService: UserService,
    private router: Router,
    private formService: FormService,
    private http: HttpClient) {
  }

  ngOnInit(): void {
    this.userName = this.userService.getUser().userName;
    this.getAllForms();
  }

  addForm(event: any) {
    const file: File = event.target.files[0]; // Get the selected file

    if (file && file.type === 'application/pdf') {
      const subject = file.name;
      const dateAdded = new Date().toISOString(); // ISO format for easier parsing

      // Combine content as a string
      const content = JSON.stringify({subject, dateAdded });

      const reader = new FileReader();
      reader.onload = () => {
        const fileBase64 = reader.result as string; // Base64 string of file

        // Send the base64 file content to backend
        this.formService.addForm(fileBase64, content, this.userName).subscribe({
          next: (response: any) => {
            console.log('Form sent successfully:', response);
            this.uploadError = null;
            this.getAllForms();
          },
          error: (error) => {
            if(error.status != 200){
              console.error('Failed to send form:', error);
              this.uploadError = 'Failed to send form to server.';
            }
            else{
              this.uploadError = null;
              this.getAllForms();
            }

          }
        });
      };

      reader.onerror = () => {
        this.uploadError = 'Error reading the file.';
      };

      reader.readAsDataURL(file); // Read file as Base64 string
    } else {
      this.uploadError = 'Only PDF files are allowed.';
    }
  }

  getAllForms() {
    this.formService.getAllForms().subscribe({
      next: (response: any[]) => {
        this.forms = response.map(form => {
          let parsedContent;
          try {
            parsedContent = JSON.parse(form.content);
          } catch (e) {
            parsedContent = {subject: 'Invalid content', dateAdded: null };
          }



          return {
            id: form.id,
            subject: parsedContent.subject,
            dateAdded: new Date(parsedContent.dateAdded).toISOString().split('T')[0],
            addedBy: form.addedBy
          };
        });

        console.log('Forms loaded:', this.forms);
      },
      error: (err) => {
        console.error('Failed to load forms:', err);
      }
    });
  }

  downloadForm(id: number): void {
    const url = `/download/${id}`;

    this.http.get(url, {
      responseType: 'arraybuffer',
      observe: 'response'
    }).subscribe(response => {
      // Extract filename from Content-Disposition header
      const contentDisposition = response.headers.get('Content-Disposition');
      let filename = `form_${id}.pdf`;

      if (contentDisposition) {
        const filenameMatch = contentDisposition.match(/filename="?(.+)"?/);
        if (filenameMatch && filenameMatch.length > 1) {
          filename = filenameMatch[1];
        }
      }

      // Create blob from the PDF data
      const blob = new Blob([response.body as ArrayBuffer], { type: 'application/pdf' });

      // Use file-saver to save the file
      saveAs(blob, filename);
    }, error => {
      console.error('Error downloading file:', error);
      // Handle error (show message to user, etc.)
    });
  }

  private saveBlobAsFile(blob: Blob, id: number): void {
    const a = document.createElement('a');
    const url = window.URL.createObjectURL(blob);
    a.href = url;
    a.download = `form_${id}.pdf`;
    a.style.display = 'none';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    setTimeout(() => window.URL.revokeObjectURL(url), 100);
  }

  private readBlobAsText(blob: Blob): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = reject;
      reader.readAsText(blob);
    });
  }




  deleteForm(id: number) {
    this.formService.deleteForm(id).subscribe({
      next: () => {
        this.getAllForms();
      },
      error: (err) => {
        console.error(`Failed to delete form with ID ${id}:`, err);
        this.getAllForms();
      }
    });
  }



}
