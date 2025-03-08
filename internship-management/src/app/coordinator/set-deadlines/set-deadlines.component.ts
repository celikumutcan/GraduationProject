import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {AnnouncementService} from '../../../services/announcement.service';
import {Router} from '@angular/router';
import {UserService} from '../../../services/user.service';
import {DeadlineService} from '../../../services/deadline.service';

@Component({
  selector: 'app-set-deadlines',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './set-deadlines.component.html',
  styleUrls: ['./set-deadlines.component.css']
})
export class SetDeadlinesComponent implements OnInit{
  internshipDeadline: string = '';
  reportDeadline: string = '';
  currentInternshipDeadline: string = '';
  currentReportDeadline: string = '';

  constructor(
    private deadlineService: DeadlineService,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.deadlineService.getInternshipDeadline().subscribe({
      next: (response: any) => {
        this.currentInternshipDeadline = response.internshipDeadline;
        console.log(response.internshipDeadline );
      },
      error: (err: any) => {
        console.error('Error fetching to internship deadline:', err);
      },
    });
    this.deadlineService.getReportDeadline().subscribe({
      next: (response: any) => {
        this.currentReportDeadline = response.reportDeadline;
        console.log(response.reportDeadline );
      },
      error: (err: any) => {
        console.error('Error fetching to report deadline:', err);
      },
    });

  }

  saveInternshipDeadline(): void {
    console.log('Internship Deadline:', this.internshipDeadline);

    this.deadlineService.postDeadline(this.internshipDeadline,this.currentReportDeadline).subscribe({
      next: (response: any) => {
        alert('Internship Deadline saved: ' + this.internshipDeadline);
        this.reload();
      },
      error: (err: any) => {
        console.error('Error saving internship deadline:', err);
        this.reload();
      },
    });
  }

  removeInternshipDeadline(): void {
    this.internshipDeadline = '';
    console.log('Internship Deadline removed');

    this.deadlineService.deleteInternshipDeadline().subscribe({
      next: (response: any) => {
        alert('Internship Deadline removed');
        this.reload();
      },
      error: (err: any) => {
        console.error('Error deleting internship deadlines:', err);
      },
    });
  }

  saveReportDeadline(): void {
    console.log('Report Deadline:', this.reportDeadline);
    this.deadlineService.postDeadline(this.currentInternshipDeadline,this.reportDeadline).subscribe({
      next: (response: any) => {
        alert('Report Deadline saved: ' + this.reportDeadline)
        this.reload();
      },
      error: (err: any) => {
        console.error('Error saving report deadline:', err);
        this.reload();
      },
    });
  }

  removeReportDeadline(): void {
    this.reportDeadline = '';
    console.log('Report Deadline removed');
    this.deadlineService.deleteReportDeadline().subscribe({
      next: (response: any) => {
        alert('Report Deadline removed');
        this.reload();
      },
      error: (err: any) => {
        console.error('Error deleting internship deadlines:', err);
      },
    });
  }

  reload():void{
    const currentUrl = this.router.url;
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigateByUrl(currentUrl);
    });
  }
}
