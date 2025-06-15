import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

// DTO’yu aynı isimle kullanırsan import edebilirsin,
// burada direkt tipi gövdeledim
export interface ReportEvaluationDTO {
  reportId: number;
  studentUserName: string;
  companyEvalGrade: number;   companyEvalComment: string;
  reportStructureGrade: number; reportStructureComment: string;
  abstractGrade: number;      abstractComment: string;
  problemStatementGrade: number; problemStatementComment: string;
  introductionGrade: number;   introductionComment: string;
  theoryGrade: number;        theoryComment: string;
  analysisGrade: number;      analysisComment: string;
  modellingGrade: number;     modellingComment: string;
  programmingGrade: number;   programmingComment: string;
  testingGrade: number;       testingComment: string;
  conclusionGrade: number;    conclusionComment: string;
  feedback: string;

}

@Injectable({
  providedIn: 'root'
})
export class EvaluateReportsService {
  private readonly API = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  /**
   * Koordinatöre atanmış formları getirir
   */
  getCoordinatorTraineeForms(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API}/internships`);
  }

  /**
   * Tüm değerlendirmeleri kaydeder (submit grading)
   */
  createAllEvaluations(dto: ReportEvaluationDTO): Observable<void> {
    return this.http.post<void>(
      `${this.API}/report-evaluations`,
      dto
    );
  }

  /**
   * Raporu reject eder, reason'u instructor feedback olarak kaydeder
   */
  rejectReport(reportId: number, reason: string): Observable<void> {
    const params = new HttpParams().set('reason', reason);
    return this.http.put<void>(
      `${this.API}/report-evaluations/${reportId}/reject`,
      null,
      { params }
    );
  }

  /**
   * Raporu "needs correction" statüsüne çeker
   */
  correctionReport(reportId: number, reason: string): Observable<void> {
    const params = new HttpParams().set('reason', reason);
    return this.http.put<void>(
      `${this.API}/report-evaluations/${reportId}/correction`,
      null,
      { params }
    );
  }
  getStudentUserName(reportId: number): Observable<string> {
    return this.http.get(`${this.API}/report-evaluations/${reportId}/student-username`, {
      responseType: 'text'
    });
  }
  downloadExcel(
    instructorUserName: string,
    startDate: string,
    endDate: string
  ): Observable<Blob> {
    const params = new HttpParams()
      .set('instructorUserName', instructorUserName)
      .set('startDate', startDate)
      .set('endDate', endDate);

    const url = `${this.API}/traineeFormInstructor/reports/download`;
    return this.http.get(url, { params, responseType: 'blob' });
  }
  /**
   * İsteğe bağlı: belirli bir reportId için
   * yapılan değerlendirmeleri getirmek istersen.
   */
  getEvaluations(reportId: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.API}/report-evaluations/${reportId}`
    );
  }

  downloadEvaluationCsv(reportId: number): Observable<Blob> {
    return this.http.get(`${this.API}/report-evaluations/${reportId}/export-excel`, {
      responseType: 'blob'
    });
  }

  downloadExcelForCoordinator(startDate: string, endDate: string): Observable<Blob> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

    const url = 'http://localhost:8080/api/traineeFormCoordinator/reports/download';
    return this.http.get(url, { params, responseType: 'blob' });
  }
}
