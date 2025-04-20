import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DataProcessingService {
  private apiUrl = 'http://localhost:8080/api/data';

  constructor(private http: HttpClient) {}

  processExcelToCsv(): Observable<{ message: string, csvPath: string }> {
    return this.http.post<{ message: string, csvPath: string }>(
      `${this.apiUrl}/process`,
      {}
    );
  }
}
