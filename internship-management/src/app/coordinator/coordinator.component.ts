import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-coordinator',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './coordinator.component.html',
  styleUrls: ['./coordinator.component.css'],
})
export class CoordinatorComponent {
  isDropdownOpen = false;

  constructor(private router: Router) {}


  toggleDropdown(): void {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown(): void {
    this.isDropdownOpen = false;
  }

  logout(): void {
    this.router.navigate(['/']); // Welcome Page'e yönlendir
    this.closeDropdown();
  }
}
