import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {EvaluateReportsService} from '../../../services/evaluate-reports.service';
import {UserService} from '../../../services/user.service';
import {Router} from '@angular/router';
import {ReportService} from '../../../services/report.service';
import {FormService} from '../../../services/form.service';
import {HttpClient} from '@angular/common/http';
import { saveAs } from 'file-saver';
import {firstValueFrom} from 'rxjs';

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
      const content = JSON.stringify({ subject, dateAdded });

      const formData = new FormData();
      formData.append('file', file); // Directly append the file
      formData.append('content', content);
      formData.append('addUserName', this.userName); // Assuming userName is set elsewhere

      this.formService.addForm(formData).subscribe({
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

  downloadForm(formId: number) {
    this.formService.downloadForm(formId).subscribe({
      next: (blob) => {
        console.log("success")
        const a = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);
        a.href = objectUrl;
        a.download = `form_${formId}.pdf`;
        a.click();
        URL.revokeObjectURL(objectUrl);
      },
      error: (err) => console.error('Error downloading form', err)
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
