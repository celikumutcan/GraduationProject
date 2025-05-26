import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Announcement {
  title: string;
  content: string;
  addedBy: string;
  datetime: string;
  id: number;
  showMenu: boolean;
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

  createAnnouncement(payload: { title: string; content: string; addUserName: string; userType: string }): Observable<any> {
    const params = new HttpParams()
      .set('title', payload.title)
      .set('content', payload.content)
      .set('addUserName', payload.addUserName)
      .set('userType', payload.userType);

    return this.http.post<any>(this.apiUrl, null, { params });
  }

  deleteAnnouncement(id: number) {
    this.apiUrl = "http://localhost:8080/api/announcements";
    console.log("Deleted Successfully");
    return this.http.delete<any>(`${this.apiUrl}/${id}`);

  }


}
