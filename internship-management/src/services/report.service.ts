import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = '';

  constructor(private http: HttpClient) {}

  /**
   * Retrieves all reports related to a trainee form.
   * @param formId ID of the trainee form
   * @returns Observable containing the list of reports
   */
  getReports(formId: number): Observable<any> {
    this.apiUrl = `http://localhost:8080/reports/trainee/${formId}/reports`;
    return this.http.get<any>(this.apiUrl);
  }

  /**
   * Uploads a report file for a trainee form.
   * @param formId ID of the trainee form
   * @param userName Username of the student
   * @param file File object to upload
   * @returns Observable with the upload result
   */
  uploadReport(formId: number, userName: string, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('traineeInformationFormId', formId.toString());
    formData.append('userName', userName);
    formData.append('file', file);

    this.apiUrl = 'http://localhost:8080/reports/upload';
    return this.http.post<any>(this.apiUrl, formData);
  }

  /**
   * Deletes a report by its ID.
   * @param id ID of the report
   * @returns Observable with the deletion result
   */
  deleteReport(id: number): Observable<any> {
    this.apiUrl = `http://localhost:8080/reports/${id}`;
    return this.http.delete<any>(this.apiUrl);
  }

  /**
   * Downloads a report file by its ID as a Blob.
   * @param id ID of the report
   * @returns Observable with the PDF file as a Blob
   */
  downloadReportFile(id: number): Observable<Blob> {
    const apiUrl = `http://localhost:8080/reports/${id}/download`;
    return this.http.get(apiUrl, { responseType: 'blob' });
  }
}
