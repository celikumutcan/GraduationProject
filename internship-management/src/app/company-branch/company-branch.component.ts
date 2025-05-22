import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { DarkModeService } from '../../services/dark-mode.service';
import { UserService } from '../../services/user.service';
import {CompanyService} from '../../services/company.service';

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
  branchId: number | undefined;

  constructor(
    public darkModeService: DarkModeService,
    private router: Router,
    private userService: UserService,
    private companyService: CompanyService
  ) {}

  ngOnInit(): void {
    const currentUser = this.userService.getUser();
    this.userName = currentUser?.userName || 'Unknown';

  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  fetchBranchId(): void {
    if (!this.userName) return;

    this.companyService.getBranchIdByUsername(this.userName).subscribe({
      next: (id) =>  {this.branchId = id;
        },
      error: (err) => {
        console.error('Error fetching branch ID', err);
      },
    });
  }

  logout(): void {
    this.router.navigate(['/']);
    this.isDropdownOpen = false;
  }

  closeDropdownAndNavigate(): void {
    this.isDropdownOpen = false;
    this.router.navigate(['/company-branch/change-password']);
  }
}
