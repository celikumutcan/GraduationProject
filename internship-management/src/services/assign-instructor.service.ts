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
}
