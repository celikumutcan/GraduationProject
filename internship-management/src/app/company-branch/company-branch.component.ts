import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { DarkModeService } from '../../services/dark-mode.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-company-branch',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './company-branch.component.html',
  styleUrls: ['./company-branch.component.css']
})

export class CompanyBranchComponent implements OnInit {
  userName: string = '';
  isDropdownOpen: boolean = false;

  constructor(
    public darkModeService: DarkModeService,
    private router: Router,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const currentUser = this.userService.getUser();
    this.userName = currentUser?.userName || 'Unknown';
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  logout(): void {
    this.router.navigate(['/']);
    this.isDropdownOpen = false;
  }
}
