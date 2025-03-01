import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Announcement} from './announcement.service';

export interface ApprovedInternships {
  id: number,
  name: string,
  lastName: string,
  username: string,
  datetime: string,
  position: string,
  type: string,
  code: string,
  semester: string,
  supervisorName: string,
  supervisorSurname: string,
  healthInsurance: true,
  insuranceApproval: false,
  insuranceApprovalDate: null,
  status: string,
  companyUserName: string,
  branchName: string,
  companyAddress: string,
  companyPhone: string,
  companyEmail: string,
  evaluateUserName: null,
  coordinatorUserName:string,
  evaluatingFacultyMember: string,
  evaluateForms: any,
  reports: InternshipReport[],
  country: string,
  city: string,
  district: string,
  internshipStartDate: string,
  internshipEndDate: string,
  applied: boolean
}
export interface InternshipReport {
  id: number,
  grade: string,
  feedback: string,
  status: null
}

@Injectable({
  providedIn: 'root'
})
export class InternshipsService {
  private apiUrl = '';

  constructor(private http: HttpClient) {}

  getApprovedInternships(): Observable<ApprovedInternships[]>{
    this.apiUrl = "http://localhost:8080/api/internships"
    return this.http.get<any>(this.apiUrl);
  }

}
