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
      { path: '', redirectTo: 'announcements', pathMatch: 'full' }, // Varsayılan olarak announcements route'una yönlendirme
      { path: 'announcements', component: AnnouncementsComponent }, // Announcements route'u
      { path: 'forms', component: FormsComponent }, // Forms route'u
      { path: 'evaluate-forms', component: EvaluateFormsComponent }, // Evaluate forms route'u
      { path: 'set-deadlines', component: SetDeadlinesComponent } // Yeni eklenen Set Deadlines route'u
    ],
  },
  {
    path: 'instructor',
    component: InstructorComponent, // Instructor için yeni route
  },
  {
    path: 'student-affairs',
    component: StudentAffairsComponent, // Student Affairs için yeni route
  },
// {
//   path: 'company-branch',
//   component: CompanyBranchComponent,
// },
  {
    path: 'student-affairs',
    component: StudentAffairsComponent,
    children: [
      { path: 'approved-internships', component: ApprovedInternshipsComponent }
    ],
  },
];

export const appRouterProviders = [provideRouter(routes)];
