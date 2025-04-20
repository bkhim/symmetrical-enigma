import { Component } from '@angular/core';
import { StudentService } from '../../core/services/student.service';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  imports: [
    MatCardHeader,
    MatCard,
    MatCardContent,
    MatCardTitle
  ],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  studentCount: number = 0;

  constructor(private studentService: StudentService) {
    this.loadStudentCount();
  }

  loadStudentCount() {
    this.studentService.getStudentCount().subscribe({
      next: (count) => this.studentCount = count,
      error: (err) => console.error(err)
    });
  }
}
