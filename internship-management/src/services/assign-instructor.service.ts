import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

// Interface defining the structure of an approved internship
export interface Instructor {
  userName: string;
  firstName: string;
  lastName: string;
}

@Injectable({
  providedIn: 'root'
})
export class AssignInstructorService {
  private apiUrl = '/api/academicStaff';

  constructor(private http: HttpClient) { }

  getInstructors(): Observable<Instructor[]> {
    return this.http.get<Instructor[]>(`${this.apiUrl}/all`);
  }

  assignInstructorManually(id: number, instructorUsername: string) {
    const body = {
      id: id.toString(),
      instructorUsername: instructorUsername,
    };

    return this.http.post<{ [key: string]: string }>(
      `/api/assignments/assign-manually`,
      body
    );
  }

  assignInstructorRandomly(assignFormIds: number[], instructors: string[]): Observable<any> {
    const payload = {
      assignFormIds,
      instructors
    };

    return this.http.post<any>(`/api/assignments/students-to-instructors`, payload);
  }
}
