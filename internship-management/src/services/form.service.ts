import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root' // Bu, servisin uygulama genelinde singleton olarak kullanılmasını sağlar.
})
export class FormService {
  private apiUrl = 'api/forms'; // API URL

  constructor(private http: HttpClient) {}


  addForm(file: string, content: string, userName: string) {
    const params = new HttpParams()
      .set('content', content)
      .set('addUserName', userName);

    return this.http.post(this.apiUrl, file, { params, responseType: 'json' });
  }

  deleteForm(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  getAllForms() {
    return this.http.get<any[]>(this.apiUrl);
  }
}
