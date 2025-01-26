import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {UserService} from '../../../services/user.service';
import {Router} from '@angular/router';
import {Announcement, AnnouncementService} from '../../../services/announcement.service';

@Component({
  selector: 'app-announcements',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './announcements.component.html',
  styleUrls: ['./announcements.component.css'],
})
export class AnnouncementsComponent implements OnInit {
  isAddFormVisible = false;
  title: string = '';
  content: string = '';
  currentUser: any;
  announcements: Announcement[] = [];
  loading = false;
  userType = ""

  constructor(
    private announcementService: AnnouncementService,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.userService.getUser();
    this.fetchAnnouncements();
    this.userType= this.currentUser.userType
  }

  toggleAddForm(): void {
    this.isAddFormVisible = !this.isAddFormVisible;
  }


  createAnnouncement(): void {
    if (!this.title || !this.content) {
      alert('Both title and content are required.');
      return;
    }
    const newAnnouncement: any = {
      title: this.title,
      content: this.content,
      addUserName: this.currentUser,
    };
    this.isAddFormVisible = false;
    this.announcementService.createAnnouncement(newAnnouncement).subscribe({

      next: (response) => {
        this.isAddFormVisible = false;
      },
      error: (err) => {
        this.isAddFormVisible = false;
      }

    });
  }

  fetchAnnouncements(): void {
    this.loading = true;
    this.announcementService.getAnnouncements().subscribe({
      next: (data) => {
        this.announcements = data.reverse();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching announcements', err);
        this.loading = false;
      }
    });
  }
}
