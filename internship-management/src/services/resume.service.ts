// resume.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ResumeService {
  private apiUrl = '';

  constructor(private http: HttpClient) {}

  // GET: Tüm CV'leri getir
  getAllResumes(): Observable<any> {
    this.apiUrl = 'http://localhost:8080/api/resumes';
    return this.http.get<any>(this.apiUrl);
  }

  // POST: CV yükle
  uploadResume(userName: string, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    this.apiUrl = `http://localhost:8080/api/resumes/upload-cv/${userName}`;
    return this.http.post<any>(this.apiUrl, formData);
  }

  // CV sil
  deleteResume(id: number): Observable<any> {
    return this.http.delete<any>(
      `http://localhost:8080/api/resumes/${id}`
    );
  }

  // CV indir
  downloadResume(username: string): Observable<Blob> {
    return this.http.get(
      `http://localhost:8080/api/resumes/download-cv/${username}`,
      { responseType: 'blob' }
    );
  }


}
