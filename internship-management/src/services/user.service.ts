import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private currentUser: any = null;

  private isLocalStorageAvailable(): boolean {
    try {
      return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
    } catch (e) {
      return false;
    }
  }

  setUser(user: any): void {
    this.currentUser = user;
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem('currentUser', JSON.stringify(user)); // Save to localStorage for persistence
    }
  }

  getUser(): any {
    if (!this.currentUser && this.isLocalStorageAvailable()) {
      this.currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');
    }
    return this.currentUser;
  }

  clearUser(): void {
    this.currentUser = null;
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem('currentUser');
    }
  }
}

