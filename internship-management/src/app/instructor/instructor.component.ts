import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../services/user.service';
import { AnnouncementService } from '../../services/announcement.service';

@Component({
  selector: 'app-instructor',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './instructor.component.html',
  styleUrls: ['./instructor.component.css']
})
export class InstructorComponent implements OnInit {
  userName: string = '';
  announcements: any[] = [];
  isDropdownOpen = false;

  constructor(
    private userService: UserService,
    private announcementService: AnnouncementService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUser = this.userService.getUser();
    this.userName = currentUser.userName; // Giriş yapan kullanıcının kullanıcı adı (örneğin: "eever")
    this.fetchAnnouncements();
  }

  fetchAnnouncements(): void {
    this.announcementService.getAnnouncements().subscribe({
      next: data => {
        this.announcements = data.reverse();
      },
      error: err => {
        console.error('Error fetching announcements', err);
      }
    });
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  logout(): void {
    this.router.navigate(['/']); // Welcome page'e yönlendirir
    this.isDropdownOpen = false;
  }

  isChildRouteActive(): boolean {
    return this.router.url !== '/instructor';
  }
}
