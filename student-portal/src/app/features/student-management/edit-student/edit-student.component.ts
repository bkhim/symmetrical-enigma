import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { StudentService } from '../../../core/services/student.service';
import { HttpResponse } from '@angular/common/http';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatDatepicker, MatDatepickerInput} from '@angular/material/datepicker';
import {MatCheckbox} from '@angular/material/checkbox';
import {MatButton} from '@angular/material/button';
import {MatNativeDateModule} from '@angular/material/core';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-edit-student',
  templateUrl: './edit-student.component.html',
  imports: [
    MatFormField,
    MatLabel,
    MatInput,
    ReactiveFormsModule,
    MatDatepickerInput,
    MatDatepicker,
    MatCheckbox,
    MatButton,
    MatNativeDateModule,
    NgIf,
  ],
  styleUrls: ['./edit-student.component.scss']
})
export class EditStudentComponent implements OnInit {
  studentForm: FormGroup;
  studentId: string = '';
  selectedFile: File | null = null;
  photoUrl: string | ArrayBuffer | null = null;

  constructor(
    private route: ActivatedRoute,
    private studentService: StudentService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.studentForm = this.fb.group({
      studentId: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dob: ['', Validators.required],
      className: ['', Validators.required],
      score: ['', [Validators.required, Validators.min(0), Validators.max(100)]],
      status: [true, Validators.required],
      photoPath: ['']
    });
  }

  ngOnInit(): void {
    this.studentId = this.route.snapshot.paramMap.get('id')!;
    this.getStudentDetails();
  }

  getStudentDetails() {
    this.studentService.getStudentDetails(this.studentId).subscribe(
      (student) => {
        this.studentForm.patchValue(student); // Populate form with student data
      },
      (error) => {
        console.error('Error loading student details:', error);
      }
    );
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  onSubmit() {
    if (this.studentForm.invalid) {
      return;
    }

    const updatedStudent = this.studentForm.value;

    if (this.selectedFile) {
      this.studentService.uploadPhoto(this.studentId, this.selectedFile).subscribe(
        (event) => {
          if (event instanceof HttpResponse) {
            updatedStudent.photoPath = event.body?.data; // Assuming the backend returns the photo URL
            this.updateStudent(updatedStudent);
          }
        },
        (error) => {
          console.error('Error uploading photo:', error);
        }
      );
    } else {
      this.updateStudent(updatedStudent);
    }
  }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input && input.files?.length) {
      const file = input.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.photoUrl = reader.result;  // Set the photo URL for preview
      };
      reader.readAsDataURL(file);  // Read the image as a data URL
    }
  }
  updateStudent(updatedStudent: any) {
    this.studentService.updateStudent(this.studentId, updatedStudent).subscribe(
      () => {
        this.router.navigate(['/student-details', this.studentId]);
      },
      (error) => {
        console.error('Error updating student:', error);
      }
    );
  }
}

