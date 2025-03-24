import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Report {
  studentName: string;
  studentId: string;
  course: string;
  reportStatus: string;
  professorComment?: string;
  assignedDate: string;
}

@Injectable({
  providedIn: 'root' // Bu, servisin uygulama genelinde singleton olarak kullanılmasını sağlar.
})
export class EvaluateReportsService {
  private apiUrl = 'https://your-backend-api/reports/assigned'; // API URL

  constructor(private http: HttpClient) {}


  downloadExcel(userName:string, startDate:string, endDate:string): Observable<Blob> {
    this.apiUrl = "http://localhost:8080/api/traineeFormInstructor/reports/download" + "?instructorUserName=" + userName + "&startDate=" + startDate+ "&endDate=" + endDate;
    return this.http.get(this.apiUrl, { responseType: 'blob' });
  }

  getCoordinatorTraineeForms() {
    this.apiUrl= "http://localhost:8080/api/internships"
    return this.http.get<any>(this.apiUrl);
  }
}
