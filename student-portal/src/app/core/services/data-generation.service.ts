import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class DataGenerationService {
  private apiUrl = 'http://localhost:8080/api/data';

  constructor(private http: HttpClient) {}

  generateStudentData(count: number): Observable<{ message: string, filePath: string }> {
    return this.http.post<{ message: string, filePath: string }>(
      `${this.apiUrl}/generate`,
      { count }
    );
  }
}
