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
  roles: string[] = ['Students', 'Instructors', 'Companies'];
  selectedRoles: string[] = [];


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

  onRoleChange(event: any) {
    const role = event.target.value;
    if (event.target.checked) {
      if(role==="Students"){
        this.selectedRoles.push("student")
      }
      else if(role==="Instructors"){
        this.selectedRoles.push("instructor")
      }
      else if(role==="Companies"){
        this.selectedRoles.push("company_branch")
      }
    } else {
      this.selectedRoles = this.selectedRoles.filter(r => r !== role);
    }
  }

  createAnnouncement(): void {
    if (!this.title || !this.content) {
      alert('Both title and content are required.');
      return;
    }
    const newAnnouncement: any = {
      title: this.title,
      content: this.content,
      addUserName: this.currentUser.userName,
      userType: this.selectedRoles
    };
    this.isAddFormVisible = false;
    this.announcementService.createAnnouncement(newAnnouncement).subscribe({

      next: (response) => {

      },
      error: (err) => {

      }

    });
  }

  fetchAnnouncements(): void {
    this.loading = true;
    this.announcementService.getAnnouncements().subscribe({
      next: (data) => {
        this.announcements = data.map(a => ({
          ...a,
          showMenu: false
        }));
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching announcements', err);
        this.loading = false;
      }
    });
  }

  toggleMenu(announcement: any) {
    // Close other menus if needed
    this.announcements.forEach(a => {
      if (a !== announcement) {
        a.showMenu = false;
      }
    });

    announcement.showMenu = !announcement.showMenu;
  }

  editAnnouncement(announcement: any) {
    console.log('Edit clicked:', announcement);
    // Your edit logic here...
  }

  deleteAnnouncement(announcement: any) {
    this.announcementService.deleteAnnouncement(announcement.id).subscribe({
      next: (data) => {
      },
      error: (err) => {
        console.error('Error fetching announcements', err);
      }
    });
    announcement.showMenu = false;
  }

}
