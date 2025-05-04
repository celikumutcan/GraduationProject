import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {EvaluateReportsService} from '../../../services/evaluate-reports.service';
import {UserService} from '../../../services/user.service';
import {Router} from '@angular/router';
import {ReportService} from '../../../services/report.service';
import {FormService} from '../../../services/form.service';

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
    private formService: FormService) {
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

          // Convert Base64 to PDF Blob URL
          const base64 = form.file;

          const byteCharacters = atob(base64.split(',')[1] || base64); // handle optional data URL prefix
          const byteNumbers = new Array(byteCharacters.length).fill(0).map((_, i) => byteCharacters.charCodeAt(i));
          const byteArray = new Uint8Array(byteNumbers);
          const pdfBlob = new Blob([byteArray], { type: 'application/pdf' });

          const pdfUrl = URL.createObjectURL(pdfBlob);

          return {
            subject: parsedContent.subject,
            dateAdded: parsedContent.dateAdded,
            file: pdfUrl,
            username: form.addUserName
          };
        });

        console.log('Forms loaded:', this.forms);
      },
      error: (err) => {
        console.error('Failed to load forms:', err);
      }
    });
  }

  deleteForm(id: number) {
    this.formService.deleteForm(id).subscribe({
      next: () => {
        // Remove the deleted form from the local array
        this.forms = this.forms.filter(form => form.id !== id);
        console.log(`Form with ID ${id} deleted.`);
        this.getAllForms();
      },
      error: (err) => {
        console.error(`Failed to delete form with ID ${id}:`, err);
        this.getAllForms();
      }
    });
  }



}
