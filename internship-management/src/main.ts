import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { DarkModeService } from './services/dark-mode.service'; // Servis yolu
import { UserService} from './services/user.service';
import {AnnouncementService} from './services/announcement.service';
import {TraineeInformationFormService} from './services/trainee-information-form.service';
import { provideHttpClient  } from '@angular/common/http';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    DarkModeService, // Servisi sağlayıcı olarak ekledim
    UserService,
    AnnouncementService,
    TraineeInformationFormService,
    provideHttpClient(),
  ],
}).catch(err => console.error(err));
