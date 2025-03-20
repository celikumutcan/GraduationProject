import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = '';
  constructor(private http: HttpClient) {}

  getReports(formId: number): Observable<any> {
    this.apiUrl= "http://localhost:8080/reports/trainee/" + formId.toString() + "/reports";
    return this.http.get<any>(this.apiUrl);
  }

  uploadReport(formId: number, userName: string, file:File ): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('traineeInformationFormId', formId.toString());  // Add formId
    formData.append('userName',userName);  // Add userName
    formData.append('file', file);  // Add file
    this.apiUrl= "http://localhost:8080/reports/upload";
    return this.http.post<any>(this.apiUrl, formData);
  }

  deleteReport(id: number) {
    this.apiUrl = `http://localhost:8080/reports/${id}`;
    return this.http.delete<any>(this.apiUrl);
  }
}
