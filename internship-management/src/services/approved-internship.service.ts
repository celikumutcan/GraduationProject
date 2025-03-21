import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interface defining the structure of an approved internship
export interface ApprovedInternship {
  id: number;
  name: string;
  lastName: string;
  username: string;
  datetime: string;
  position: string;
  type: string;
  code: string;
  semester: string;
  supervisorName?: string;
  supervisorSurname?: string;
  healthInsurance: boolean;
  insuranceApproval: boolean;
  insuranceApprovalDate?: string;
  status: string;
  companyUserName: string;
  branchName: string;
  companyAddress: string;
  companyPhone: string;
  companyEmail: string;
  internshipStartDate?: string;
  internshipEndDate?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApprovedInternshipService {
  private apiUrl = '/api/studentAffairs';

  constructor(private http: HttpClient) {}

  // Fetches all approved internships from the backend
  getApprovedInternships(): Observable<ApprovedInternship[]> {
    return this.http.get<ApprovedInternship[]>(`${this.apiUrl}/approvedInternships`);
  }

  // Approves insurance for a specific internship
  approveInsurance(internshipId: number, approvedBy: string): Observable<string> {
    const params = new HttpParams()
      .set('internshipId', internshipId.toString())
      .set('approvedBy', approvedBy);

    return this.http.post<string>(`${this.apiUrl}/approveInsurance`, {}, { params });
  }

  // Exports approved internships data to an Excel file
  exportToExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/exportApprovedInternships`, { responseType: 'blob' });
  }
}
