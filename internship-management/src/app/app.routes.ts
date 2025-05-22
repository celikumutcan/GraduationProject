import { provideRouter, Routes } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';
import { StudentComponent } from './student/student.component';
import { MyResumeComponent } from './student/my-resume.component';
import { CheckFormsComponent } from './student/check-forms/check-forms.component';
import { BrowseInternshipsComponent } from './student/browse-internships/browse-internships.component';
import { CoordinatorComponent } from './coordinator/coordinator.component';
import { FormsComponent } from './coordinator/forms/forms.component';
import { AnnouncementsComponent } from './coordinator/announcements/announcements.component';
import { EvaluateFormsComponent } from './coordinator/evaluate-forms/evaluate-forms.component';
import { SetDeadlinesComponent } from './coordinator/set-deadlines/set-deadlines.component';
import { InstructorComponent } from './instructor/instructor.component';
import { StudentAffairsComponent } from './student-affairs/student-affairs.component';
import { ApprovedInternshipsComponent } from './student-affairs/approved-internships/approved-internships.component';
import { EvaluateAssignedReportsComponent } from './instructor/evaluate-assigned-reports/evaluate-assigned-reports.component';

import { CompanyBranchComponent } from './company-branch/company-branch.component';
import { EvaluateInternStudentComponent } from './company-branch/evaluate-intern-student/evaluate-intern-student.component';
import { MyOffersComponent } from './company-branch/my-offers/my-offers.component';

export const routes: Routes = [
  { path: '', component: WelcomeComponent },

  // 🎓 STUDENT
  {
    path: 'student',
    component: StudentComponent,
    children: [
      { path: 'my-resume', component: MyResumeComponent },
      { path: 'check-forms', component: CheckFormsComponent },
      { path: 'browse-internships', component: BrowseInternshipsComponent },
      {
        path: 'open-internships',
        loadComponent: () =>
          import('./student/open-internships/open-internships.component').then(
            (m) => m.OpenInternshipsComponent
          ),
      },
    ],
  },

  // 👨‍🏫 COORDINATOR
  {
    path: 'coordinator',
    component: CoordinatorComponent,
    children: [
      { path: '', redirectTo: 'announcements', pathMatch: 'full' },
      { path: 'announcements', component: AnnouncementsComponent },
      { path: 'forms', component: FormsComponent },
      { path: 'evaluate-forms', component: EvaluateFormsComponent },
      { path: 'set-deadlines', component: SetDeadlinesComponent },
      {
        path: 'assign-instructors',
        loadComponent: () =>
          import('./coordinator/assign-instructors/assign-instructors.component').then(
            (m) => m.AssignInstructorsComponent
          ),
      },
    ],
  },

  // 🧑‍🏫 INSTRUCTOR
  {
    path: 'instructor',
    component: InstructorComponent,
    children: [
      {
        path: 'evaluate-assigned-reports',
        component: EvaluateAssignedReportsComponent,
      },
    ],
  },

  // 🏢 COMPANY BRANCH
  {
    path: 'company-branch',
    component: CompanyBranchComponent,
    children: [
      { path: '', redirectTo: 'my-offers', pathMatch: 'full' },
      { path: 'my-offers', component: MyOffersComponent },
      {
        path: 'create-offer',
        loadComponent: () =>
          import('./company-branch/create-offer/create-offer.component').then(
            (m) => m.CreateOfferComponent
          ),
      },
      {
        path: 'offer-applicants/:offerId',
        loadComponent: () =>
          import('./company-branch/offer-applicants/offer-applicants.component').then(
            (m) => m.OfferApplicantsComponent
          ),
      },
      { path: 'evaluate-intern-student', component: EvaluateInternStudentComponent },
    ],
  },

  // 🎓 STUDENT AFFAIRS
  {
    path: 'student-affairs',
    component: StudentAffairsComponent,
    children: [
      { path: 'approved-internships', component: ApprovedInternshipsComponent },
    ],
  },

  // 🔐 COMPANY LOGIN
  {
    path: 'company-branch/login',
    loadComponent: () =>
      import('./company-branch/company-login/company-login.component').then(
        (m) => m.CompanyLoginComponent
      ),
  },
];

export const appRouterProviders = [provideRouter(routes)];
