import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Announcement } from './announcement.service';

export interface BrowseApprovedInternships {
  id: number,
  name: string,
  lastName: string,
  username: string,
  datetime: string,
  position: string,
  type: string,
  code: string,
  semester: string,
  companyUserName: string,
  branchName: string,
  companyAddress: string,
  companyPhone: string,
  companyEmail: string,
  country: string,
  city: string,
  district: string,
  internshipStartDate: string,
  internshipEndDate: string,
  applied: boolean
}

export interface InternshipApplication {
  id: number;
  studentUsername: string;
  branchName: string;
  position: string;
  applicationDate: string;
  status: string;
  internshipOfferId: number | null;
}


@Injectable({
  providedIn: 'root'
})
export class InternshipsService {
  private apiUrl = '';

  constructor(private http: HttpClient) {}

  // Fetches Internships to Browse
  getApprovedInternships(): Observable<BrowseApprovedInternships[]> {
    this.apiUrl = "http://localhost:8080/api/internships";
    return this.http.get<any>(this.apiUrl);
  }

  // Fetches Internship Applications of The Student (OLD)
  getInternshipApplications(userName: string): any {
    this.apiUrl = "http://localhost:8080/api/internship-applications/student/".concat(userName);
    return this.http.get<any>(this.apiUrl);
  }

  // Fetches Internship Applications as DTO (NEW)
  getInternshipApplicationsDTO(userName: string): Observable<InternshipApplication[]> {
    const url = `http://localhost:8080/api/internship-applications/student-dto/${userName}`;
    return this.http.get<InternshipApplication[]>(url);
  }

  // Apply Internship
  postApplyInternship(userName: string, internshipId: number): Observable<string> {
    this.apiUrl = "http://localhost:8080/api/internship-applications/applyForInternship";
    const params = new HttpParams()
      .set('studentUsername', userName)
      .set('internshipID', internshipId.toString());
    console.log(userName, internshipId);
    return this.http.post(this.apiUrl, null, { params, responseType: 'text' });
  }
}
