import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-set-deadlines',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './set-deadlines.component.html',
  styleUrls: ['./set-deadlines.component.css']
})
export class SetDeadlinesComponent {
  internshipDeadline: string = '';
  reportDeadline: string = '';

  saveInternshipDeadline(): void {
    console.log('Internship Deadline:', this.internshipDeadline);
    alert('Internship Deadline saved: ' + this.internshipDeadline);
    // API çağrısı veya ek işlemler burada yapılabilir.
  }

  removeInternshipDeadline(): void {
    this.internshipDeadline = '';
    console.log('Internship Deadline removed');
    alert('Internship Deadline removed');
  }

  saveReportDeadline(): void {
    console.log('Report Deadline:', this.reportDeadline);
    alert('Report Deadline saved: ' + this.reportDeadline);
    // API çağrısı veya ek işlemler burada yapılabilir.
  }

  removeReportDeadline(): void {
    this.reportDeadline = '';
    console.log('Report Deadline removed');
    alert('Report Deadline removed');
  }
}
