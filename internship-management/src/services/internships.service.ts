import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

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

export interface InternshipOffer {
  offerId: number;
  position: string;
  department: string;
  startDate: string;
  endDate: string;
  details: string;
  description?: string;
}

@Injectable({
  providedIn: 'root'
})
export class InternshipsService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getApprovedInternships(): Observable<BrowseApprovedInternships[]> {
    return this.http.get<BrowseApprovedInternships[]>(`${this.baseUrl}/internships`);
  }

  getInternshipApplications(userName: string): any {
    return this.http.get<any>(`${this.baseUrl}/internship-applications/student/${userName}`);
  }

  getInternshipApplicationsDTO(userName: string): Observable<InternshipApplication[]> {
    return this.http.get<InternshipApplication[]>(`${this.baseUrl}/internship-applications/student-dto/${userName}`);
  }

  postApplyInternship(userName: string, internshipId: number): Observable<string> {
    const params = new HttpParams()
      .set('studentUsername', userName)
      .set('internshipID', internshipId.toString());
    return this.http.post(`${this.baseUrl}/internship-applications/applyForInternship`, null, {
      params,
      responseType: 'text'
    });
  }

  postApplyToInternshipOffer(userName: string, offerId: number): Observable<string> {
    const params = new HttpParams()
      .set('studentUsername', userName)
      .set('offerId', offerId.toString());
    return this.http.post(`${this.baseUrl}/internship-applications/applyForOffer`, null, {
      params,
      responseType: 'text'
    });
  }

  createInternshipOffer(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/internship-offers/create`, data);
  }

  getOpenInternshipOffers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/internship-offers/open`);
  }

  hasAppliedToOffer(username: string, offerId: number): Observable<boolean> {
    const url = `${this.baseUrl}/internship-applications/has-applied`;
    const params = new HttpParams()
      .set('studentUsername', username)
      .set('offerId', offerId.toString());
    return this.http.get<boolean>(url, { params });
  }

  /** ✅ NEW: Get offers created by a company */
  getCompanyInternshipOffers(username: string): Observable<InternshipOffer[]> {
    return this.http.get<InternshipOffer[]>(`${this.baseUrl}/internship-offers/company/${username}`);
  }

  /** ✅ NEW: Get applicants for a specific offer */
  getOfferApplicants(offerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/internship-applications/offer/${offerId}/with-cv`);
  }

  approveInternshipApplication(applicationId: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/internship-applications/approve/${applicationId}`, {});
  }

  rejectInternshipApplication(applicationId: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/internship-applications/reject/${applicationId}`, {});
  }

}
