import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DarkModeService {
  isDarkMode: boolean;

  constructor() {
    // Tarayıcıdan Dark Mode durumunu yükle
    const savedMode = localStorage.getItem('darkMode');
    this.isDarkMode = savedMode === 'true';
    this.applyDarkMode();
  }

  toggleDarkMode(): void {
    this.isDarkMode = !this.isDarkMode;
    localStorage.setItem('darkMode', String(this.isDarkMode)); // Durumu kaydet
    this.applyDarkMode();
  }

  private applyDarkMode(): void {
    if (this.isDarkMode) {
      document.body.classList.add('dark');
    } else {
      document.body.classList.remove('dark');
    }
  }
}
