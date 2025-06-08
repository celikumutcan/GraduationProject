import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { DarkModeService } from '../../services/dark-mode.service';
import { UserService } from '../../services/user.service';
import { CompanyService } from '../../services/company.service';
import { HttpClient } from '@angular/common/http';

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
  showInactivePopup: boolean = false;

  constructor(
    public darkModeService: DarkModeService,
    private router: Router,
    private userService: UserService,
    private companyService: CompanyService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    const currentUser = this.userService.getUser();
    this.userName = currentUser?.userName || 'Unknown';

    this.fetchBranchIdAndCheckStatus();
  }

  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  fetchBranchIdAndCheckStatus(): void {
    if (!this.userName) return;

    this.companyService.getBranchIdByUsername(this.userName).subscribe({
      next: (id) => {
        this.branchId = id;
        console.log('BRANCH ID:', id);
        this.checkIfInactive(id);
      },
      error: (err) => {
        console.error('Error fetching branch ID', err);
      },
    });
  }

  checkIfInactive(branchId: number): void {
    this.http.get<boolean>(`http://localhost:8080/api/company-branch/isInactive/${branchId}`).subscribe({
      next: (isInactive) => {
        console.log('IS INACTIVE:', isInactive);
        if (isInactive) {
          this.showInactivePopup = true;
        }
      },
      error: (err) => {
        console.error('Error checking inactivity', err);
      }
    });
  }

verifyNow(): void {
  console.log('✅ verifyNow triggered');

  if (!this.branchId) {
    console.warn('⚠️ Branch ID is missing!');
    return;
  }

  this.http.put(`http://localhost:8080/api/company-branch/verify/${this.branchId}`, {}).subscribe({
    next: () => {
      console.log('✅ Backend verification successful');
      alert('✅ Verification successful! Your branch is now marked as active.');
      this.showInactivePopup = false;


      location.reload(); // → opsiyonel
    },
    error: (err) => {
      console.error('❌ Error verifying branch', err);
      alert('❌ An error occurred while verifying. Please try again.');
    }
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
