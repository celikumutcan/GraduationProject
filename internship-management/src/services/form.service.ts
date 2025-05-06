import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, map, Observable, tap} from 'rxjs';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root' // Bu, servisin uygulama genelinde singleton olarak kullanılmasını sağlar.
})
export class FormService {
  private apiUrl = 'http://localhost:8080/api/forms'; // API URL

  constructor(private http: HttpClient) {}


  addForm(formData: FormData) {
    return this.http.post(`${this.apiUrl}/upload`, formData);
  }


  deleteForm(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }


  downloadForm(id: number) {
    const apiUrl = "http://localhost:8080/api/forms/download/" + id;
    return this.http.get(apiUrl, {
      responseType: 'blob',
    });
  }


  getAllForms() {
    return this.http.get<any[]>(this.apiUrl);
  }
}
