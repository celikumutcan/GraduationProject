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

  // Backend'den atanmış raporları çekmek için HTTP GET isteği yapan metod
  getAssignedReports(): Observable<Report[]> {
    return this.http.get<Report[]>(this.apiUrl);
  }
}
