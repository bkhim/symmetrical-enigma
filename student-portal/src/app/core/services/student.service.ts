import {Injectable} from '@angular/core';
import {HttpClient, HttpParams } from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../../models/page.model';

@Injectable({ providedIn: 'root' })
export class StudentService {
  private apiUrl = 'http://localhost:8080/api/students';

  constructor(private http: HttpClient) {}

  getStudentCount(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  generateData(count: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/generate`, { count });
  }

  getStudents(page: number = 0, size: number = 10, filters?: any): Observable<Page<any>> {
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    if (filters) {
      // Add other filter parameters as needed
      for (const key in filters) {
        if (filters[key]) {
          params = params.set(key, filters[key]);
        }
      }
    }

    return this.http.get<Page<any>>(`${this.apiUrl}`, { params });
  }

  getClasses(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/classes`);
  }

  getStudentDetails(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  updateStudent(id: string, student: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, student);
  }

  deleteStudent(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  uploadPhoto(id: string, file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.apiUrl}/${id}/upload-photo`, formData);
  }


}
