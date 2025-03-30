import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface InternStudent {
  id: number;
  studentName: string;
  studentSurname: string;
  position: string;
  internshipStart: string;
  internshipEnd: string;
  // Diğer alanlar (örneğin, notlar, staj raporu durumu, vb.) eklenebilir.
}

@Injectable({
  providedIn: 'root'
})
export class EvaluateInternStudentService {
  
  private apiUrl = 'http://localhost:8080/api/traineeFormCompany';

  constructor(private http: HttpClient) {}

  // Şirketin userName'ine göre staj yapmış öğrencileri çekiyoruz.
  getInternStudents(companyUserName: string): Observable<InternStudent[]> {
    const url = `${this.apiUrl}?companyUserName=${encodeURIComponent(companyUserName)}`;
    return this.http.get<InternStudent[]>(url);
  }
}
