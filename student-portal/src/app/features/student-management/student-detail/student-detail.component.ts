import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { StudentService } from '../../../core/services/student.service';
import {DatePipe, NgIf} from '@angular/common';

@Component({
  selector: 'app-student-detail',
  templateUrl: './student-detail.component.html',
  imports: [
    NgIf,
    DatePipe
  ],
  styleUrls: ['./student-detail.component.scss']
})
export class StudentDetailComponent implements OnInit {
  student: any;

  constructor(
    private studentService: StudentService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const studentId = this.route.snapshot.paramMap.get('id');
    if (studentId) {
      this.studentService.getStudentDetails(studentId).subscribe(
        (data) => {
          this.student = data;
        },
        (error) => {
          console.error('Error fetching student details', error);
        }
      );
    }
  }
}
