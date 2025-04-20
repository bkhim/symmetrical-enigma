import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {response} from 'express';

@Injectable({ providedIn: 'root' })
export class FileUploadService {
  private apiUrl = 'http://localhost:8080/api/students/upload';

  constructor(private http: HttpClient) {}

  // Method to upload the Excel file
  uploadFile(formData: FormData): Observable<any> {
    return this.http.post<any>(this.apiUrl, formData);
  }
}
