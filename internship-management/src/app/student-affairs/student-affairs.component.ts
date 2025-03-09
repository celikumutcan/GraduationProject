import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { DarkModeService } from '../../services/dark-mode.service';
import { UserService } from '../../services/user.service';
import { Announcement, AnnouncementService } from '../../services/announcement.service';

@Component({
  selector: 'app-student-affairs',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './student-affairs.component.html',
  styleUrls: ['./student-affairs.component.css'],
})
export class StudentAffairsComponent implements OnInit {
  isDropdownOpen = false;
  announcements: Announcement[] = [];
  loading = false;
  userName = "";

  constructor(
    public darkModeService: DarkModeService,
    private announcementService: AnnouncementService,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const currentUser = this.userService.getUser();
    this.userName = currentUser?.userName || "Unknown";

    // Fetch announcements only if on the main dashboard
    if (this.isMainPage()) {
      this.fetchAnnouncements();
    }
  }

  // Fetch announcements from the service
  fetchAnnouncements(): void {
    this.loading = true;
    this.announcementService.getAnnouncements().subscribe({
      next: (data) => {
        console.log("Announcements received:", data);
        if (data && Array.isArray(data)) {
          this.announcements = [...data].reverse(); // Reverse order to show latest first
          console.log("Reversed Announcements:", this.announcements);
        } else {
          console.warn("No announcements found.");
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching announcements:', err);
        this.loading = false;
      }
    });
  }

  // Check if the current route is the main Student Affairs page
  isMainPage(): boolean {
    return this.router.url === '/student-affairs';
  }

  // Toggle dropdown visibility
  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  // Close dropdown
  closeDropdown(): void {
    this.isDropdownOpen = false;
  }

  // Navigate to a route and close dropdown
  navigateAndClose(route: string): void {
    this.router.navigate([route]);
    this.closeDropdown();
  }

  // Check if a child route is active
  isChildRouteActive(): boolean {
    return this.router.url !== '/student-affairs';
  }

  // Logout and redirect to home
  logout(): void {
    this.router.navigate(['/']);
    this.closeDropdown();
  }
}
