import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DarkModeService } from '../services/dark-mode.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  constructor(public darkModeService: DarkModeService) {}

  toggleDarkMode() {
    this.darkModeService.toggleDarkMode();
  }
}
