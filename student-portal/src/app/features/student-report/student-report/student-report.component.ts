import { Component, OnInit } from '@angular/core';
import {
  MatCell, MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef,
  MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { FormGroup, FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StudentService } from '../../../core/services/student.service';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import {DatePipe, NgForOf} from '@angular/common';
import { MatOption } from '@angular/material/core';
import {MatDatepickerModule, MatDateRangeInput} from '@angular/material/datepicker';
import { MatButton } from '@angular/material/button';
import * as XLSX from 'xlsx';
import {Page} from '../../../models/page.model';
import { MatNativeDateModule } from '@angular/material/core'; // Import the DateAdapter provider

@Component({
  selector: 'app-student-report',
  standalone: true,
  imports: [
    MatFormField,
    MatLabel,
    MatInput,
    FormsModule,
    MatSelect,
    NgForOf,
    MatOption,
    MatDateRangeInput,
    ReactiveFormsModule,
    MatButton,
    MatTable,
    MatHeaderCell,
    MatCell,
    MatHeaderRow,
    MatRow,
    MatPaginator,
    MatColumnDef,
    MatHeaderCellDef,
    MatSort,
    MatCellDef,
    MatHeaderRowDef,
    MatRowDef,
    DatePipe,
    MatNativeDateModule,
    MatDatepickerModule
  ],
  templateUrl: './student-report.component.html',
  styleUrls: ['./student-report.component.scss']
})
export class StudentReportComponent implements OnInit {
  students: Page<any> = { content: [], totalElements: 0, sort:[], totalPages: 0, size: 0, first: false, last: false, number: 0 };
  displayedColumns: string[] = ['studentId', 'name', 'dob', 'className'];
  dataSource: MatTableDataSource<any> = new MatTableDataSource();
  searchStudentId: string = '';
  selectedClass: string = '';
  dobFormGroup!: FormGroup;
  classes: string[] = [];

  constructor(
    private studentService: StudentService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.dobFormGroup = this.fb.group({
      studentId: [''],
      selectedClass: [''],
      dobRange: this.fb.group({
        start: [null],
        end: [null]
      })
    });

    this.loadClasses();
    this.loadStudents();

    // Optional: react to changes in filters automatically
    this.dobFormGroup.valueChanges.subscribe(() => {
      this.applyFilters();
    });
  }

  loadClasses() {
    this.studentService.getClasses().subscribe((response: string[]) => {
      this.classes = response;
    });
  }


  loadStudents() {
    this.studentService.getStudents(0, 10).subscribe((response: Page<any>) => {
      this.students = response;
      this.dataSource.data = this.students.content;  // Set the students data for the table
    });
  }

  applyFilters() {
    const studentId = this.dobFormGroup.get('studentId')?.value;
    const selectedClass = this.dobFormGroup.get('selectedClass')?.value;
    const dobRange = this.dobFormGroup.get('dobRange')?.value;
    const { start, end } = dobRange || {};

    let filteredData = this.students.content;

    if (studentId) {
      filteredData = filteredData.filter(student =>
        student.studentId.toLowerCase().includes(studentId.toLowerCase())
      );
    }

    if (selectedClass) {
      filteredData = filteredData.filter(student =>
        student.className === selectedClass
      );
    }

    if (start && end) {
      filteredData = filteredData.filter(student => {
        const dob = new Date(student.dob);
        return dob >= new Date(start) && dob <= new Date(end);
      });
    }

    this.dataSource.data = filteredData;
  }


  exportToExcel() {
    const dataToExport = this.students.content.map((student: { studentId: any; firstName: any; lastName: any; dob: any; className: any }) => ({
      studentId: student.studentId,
      name: `${student.firstName} ${student.lastName}`,
      dob: student.dob,
      className: student.className
    }));

    const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(dataToExport);
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Students');
    XLSX.writeFile(wb, 'Student_Report.xlsx');
  }

  onPageChange(event: any) {
    // Handle pagination change if needed
    console.log('Page changed:', event);
  }
}
