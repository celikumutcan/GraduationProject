import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {Router} from '@angular/router';
import {FormService} from '../../../services/form.service';
import {HttpClient} from '@angular/common/http';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-internship-forms',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './internship-forms.component.html',
  styleUrl: './internship-forms.component.css'
})
export class InternshipFormsComponent implements OnInit {
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

  getAllForms() {
    this.formService.getAllForms().subscribe({
      next: (response: any[]) => {
        this.forms = response.map(form => {
          let parsedContent;
          try {
            parsedContent = JSON.parse(form.content);
          } catch (e) {
            parsedContent = {subject: 'Invalid content', dateAdded: null};
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
}
