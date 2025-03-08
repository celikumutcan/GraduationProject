import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DeadlineService {
  apiUrl =''

  constructor(private http: HttpClient) { }


  getInternshipDeadline():any{
    this.apiUrl = "http://localhost:8080/api/deadlines/internship-deadline";
    return this.http.get<any>(this.apiUrl);
  }

  getReportDeadline():any{
    this.apiUrl = "http://localhost:8080/api/deadlines/report-deadline";
    return this.http.get<any>(this.apiUrl);
  }

  postDeadline(internshipDeadline: string, reportDeadline: string) {
    this.apiUrl= "http://localhost:8080/api/deadlines/add";
    let payload = {"internshipDeadline": internshipDeadline, "reportDeadline": reportDeadline};
    console.log(payload);
    return this.http.post<any>(this.apiUrl, payload);
  }

  deleteInternshipDeadline():any{
    this.apiUrl = "http://localhost:8080/api/deadlines/delete/internship-deadline";
    return this.http.delete<any>(this.apiUrl);
  }

  deleteReportDeadline():any{
    this.apiUrl = "http://localhost:8080/api/deadlines/delete/report-deadline";
    return this.http.delete<any>(this.apiUrl);
  }

}
