// main.ts
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { DarkModeService } from './services/dark-mode.service';
import { UserService } from './services/user.service';
import { AnnouncementService } from './services/announcement.service';
import { TraineeInformationFormService } from './services/trainee-information-form.service';
import { provideHttpClient } from '@angular/common/http';

// FormsModule'ü sağlayıcılara ekleyebilmek için import edin
import { importProvidersFrom } from '@angular/core';
import { FormsModule } from '@angular/forms';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    DarkModeService,
    UserService,
    AnnouncementService,
    TraineeInformationFormService,
    provideHttpClient(),
    // FormsModule’ü standalone olarak ekleyin
    importProvidersFrom(FormsModule),
  ],
}).catch(err => console.error(err));
