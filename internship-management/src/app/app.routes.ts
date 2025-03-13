import { provideRouter, Routes } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { StudentComponent } from './student/student.component';
import { MyInternshipsComponent } from './student/my-internships.component';
import { MyResumeComponent } from './student/my-resume.component';
import { CheckFormsComponent } from './student/check-forms/check-forms.component';
import { BrowseInternshipsComponent } from './student/browse-internships/browse-internships.component';
import { CoordinatorComponent } from './coordinator/coordinator.component';
import { FormsComponent } from './coordinator/forms/forms.component';
import { AnnouncementsComponent } from './coordinator/announcements/announcements.component';
import { EvaluateFormsComponent } from './coordinator/evaluate-forms/evaluate-forms.component';

// Yeni eklenen componentleri import et
import { SetDeadlinesComponent } from './coordinator/set-deadlines/set-deadlines.component';
import { InstructorComponent } from './instructor/instructor.component';
import { StudentAffairsComponent } from './student-affairs/student-affairs.component';
//import { CompanyBranchComponent } from './company-branch/company-branch.component';
import { ApprovedInternshipsComponent } from './student-affairs/approved-internships/approved-internships.component';
import { EvaluateAssignedReportsComponent } from './instructor/evaluate-assigned-reports/evaluate-assigned-reports.component';

export const routes: Routes = [
  { path: '', component: WelcomeComponent }, // Ana sayfa
  {
    path: 'student',
    component: StudentComponent,
    children: [
      { path: 'my-internships', component: MyInternshipsComponent },
      { path: 'my-resume', component: MyResumeComponent },
      { path: 'check-forms', component: CheckFormsComponent },
      { path: 'browse-internships', component: BrowseInternshipsComponent },
    ],
  },
  {
    path: 'coordinator',
    component: CoordinatorComponent,
    children: [
      { path: '', redirectTo: 'announcements', pathMatch: 'full' }, // Varsayılan: announcements
      { path: 'announcements', component: AnnouncementsComponent },
      { path: 'forms', component: FormsComponent },
      { path: 'evaluate-forms', component: EvaluateFormsComponent },
      { path: 'set-deadlines', component: SetDeadlinesComponent }
    ],
  },
  {
    path: 'instructor',
    component: InstructorComponent,
    children: [
      // Eğer child route aktif değilse InstructorComponent, kendi içindeki (örn. announcements) içeriği gösterir.
      { path: 'evaluate-assigned-reports', component: EvaluateAssignedReportsComponent }
    ]
  },
  {
    path: 'student-affairs',
    component: StudentAffairsComponent,
    children: [
      { path: 'approved-internships', component: ApprovedInternshipsComponent }
    ],
  },
];

export const appRouterProviders = [provideRouter(routes)];
