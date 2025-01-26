import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { DarkModeService } from '../../services/dark-mode.service';
import {UserService} from '../../services/user.service';
import {Announcement, AnnouncementService} from '../../services/announcement.service';

@Component({
  selector: 'app-student',
  standalone: true,
  imports: [CommonModule, RouterModule], // CommonModule ve RouterModule eklendi
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css'],
})
export class StudentComponent implements OnInit {
  isDropdownOpen = false;
  announcements: Announcement[] = [];

  isAddFormVisible = false;
  title: string = '';
  content: string = '';
  currentUser: any;
  loading = false;
  userType = ""
  userName = "";
  constructor(public darkModeService: DarkModeService, private announcementService: AnnouncementService, private router: Router, private userService: UserService) {}

  ngOnInit(): void {
    this.currentUser = this.userService.getUser();
    this.fetchAnnouncements();
    this.userName = this.currentUser.userName;
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

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown(): void {
    this.isDropdownOpen = false;
  }

  navigateAndClose(route: string): void {
    this.router.navigate([route]);
    this.closeDropdown();
  }

  // Alt rota aktif mi kontrol eder
  isChildRouteActive(): boolean {
    return this.router.url !== '/student';
  }

  // Logout işlemi
  logout(): void {
    this.router.navigate(['/']); // Kullanıcıyı WelcomeComponent'e yönlendirir
    this.closeDropdown();
  }
}
