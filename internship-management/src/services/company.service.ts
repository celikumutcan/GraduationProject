import { Injectable } from '@angular/core';
import {InternshipApplication} from './internships.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';

export interface EvaluateFormDTO {
  id: number;
  attendance: string;
  diligenceAndEnthusiasm: string;
  contributionToWorkEnvironment: string;
  overallPerformance: string;
  comments: string;
}

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

  approveInternship(internshipId: number): Observable<string> {
    const url = '/api/traineeFormCompany/approveInternship';
    const params = new HttpParams().set('internshipId', internshipId.toString());
    return this.http.post(url, null, { params, responseType: 'text' });
  }

  rejectInternship(internshipId: number): Observable<string> {
    const url = '/api/traineeFormCompany/rejectInternship';
    const params = new HttpParams().set('internshipId', internshipId.toString());
    return this.http.post(url, null, { params, responseType: 'text' });
  }


  updateSupervisor(formId: number, name: string, surname: string): Observable<string> {
    const params = new HttpParams()
      .set('formId', formId.toString())
      .set('name', name)
      .set('surname', surname);
    const url = '/api/internships/updateSupervisor';

    return this.http.post(url, null, { params, responseType: 'text' });
  }

  createEvaluationForm(data: {
    traineeFormId: number,
    attendance: string,
    diligenceAndEnthusiasm: string,
    contributionToWorkEnvironment: string,
    overallPerformance: string,
    comments: string
  }): Observable<string> {
    const params = new HttpParams()
      .set('traineeFormId', data.traineeFormId.toString())
      .set('attendance', data.attendance)
      .set('diligenceAndEnthusiasm', data.diligenceAndEnthusiasm)
      .set('contributionToWorkEnvironment', data.contributionToWorkEnvironment)
      .set('overallPerformance', data.overallPerformance)
      .set('comments', data.comments);

    return this.http.post('/api/evaluation/create', null, { params, responseType: 'text' });
  }

  getEvaluationByTraineeFormId(traineeFormId: number): Observable<EvaluateFormDTO[]> {
    return this.http.get<EvaluateFormDTO[]>(`http://localhost:8080/api/evaluation/trainee/${traineeFormId}`);
  }

}
