import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StudentReportComponent } from './student-report/student-report.component';
import {MatDatepickerModule} from '@angular/material/datepicker';  // Import standalone component

const routes: Routes = [
  { path: 'student-report', component: StudentReportComponent }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    MatDatepickerModule,
  ]
})
export class StudentReportModule {}
