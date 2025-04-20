import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatCard } from '@angular/material/card';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

import { StudentService } from '../../../core/services/student.service';
import {ConfirmDialogComponent} from '../../../shared/confirm-dialog.component';
import {Page} from '../../../models/page.model';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-student-list',
  imports: [
    MatCard,
    MatTable,
    MatPaginator,
    MatIconButton,
    MatIcon,
    RouterLink,
    MatHeaderCell,
    MatColumnDef,
    MatHeaderCellDef,
    MatCellDef,
    MatCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
  ],
  templateUrl: './student-list.component.html'
})
export class StudentListComponent implements OnInit {
  students: Page<any> | undefined;
  currentPage = 0;
  pageSize = 10;

  constructor(
    private studentService: StudentService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    this.studentService.getStudents(this.currentPage, this.pageSize)
      .subscribe((data: any) => this.students = data);
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadStudents();
  }

  confirmDelete(id: string): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { title: 'Confirm Delete', message: 'Are you sure you want to delete this student?' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.studentService.deleteStudent(id).subscribe(() => this.loadStudents());
      }
    });
  }
}
