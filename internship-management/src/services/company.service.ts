import { Injectable } from '@angular/core';
import {InternshipApplication} from './internships.service';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private baseUrl = '/api/internship-applications'; // Adjust base URL as needed

  constructor(private http: HttpClient) {}

  getCompanyApplications(branchId: number | undefined): Observable<InternshipApplication[]> {
    if (branchId === undefined || branchId === null) {
      return of([]);
    }
  const url = `${this.baseUrl}/company/${branchId}/with-cv`;
    return this.http.get<InternshipApplication[]>(url);
  }

  getBranchIdByUsername(username: string): Observable<number> {
    const url = `/api/company-branch/id-from-username/${username}`;
    return this.http.get<number>(url);
  }

}
