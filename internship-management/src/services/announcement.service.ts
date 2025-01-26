import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Announcement {
  title: string;
  content: string;
  addedBy: string;
  datetime: string;
}

@Injectable({
  providedIn: 'root',
})
export class AnnouncementService {
  private apiUrl = 'http://localhost:8080/api/announcements';

  constructor(private http: HttpClient) {}

  getAnnouncements(): Observable<Announcement[]>{
    return this.http.get<Announcement[]>(this.apiUrl);
  }

  createAnnouncement(payload: { title: string; content: string; addUserName: string }): Observable<any> {
    return this.http.post<any>(this.apiUrl, payload); // Pass the full payload
  }

}
