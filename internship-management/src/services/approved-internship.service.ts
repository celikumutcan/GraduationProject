import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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

  getApprovedInternships(): Observable<ApprovedInternship[]> {
    return this.http.get<ApprovedInternship[]>(`${this.apiUrl}/approvedInternships`);
  }

  approveInsurance(internshipId: number): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/approveInsurance?internshipId=${internshipId}`, {});
  }

  exportToExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/exportApprovedInternships`, { responseType: 'blob' });
  }
}
