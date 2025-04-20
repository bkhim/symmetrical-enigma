import {Routes} from '@angular/router';
import {LoginComponent} from './features/login/login.component';

import {DashboardComponent} from './features/dashboard/dashboard.component';
import {AuthGuard} from './core/guards/auth.guard';
import {DataGenerationComponent} from './features/data-generation/data-generation.component';
import {DataProcessingComponent} from './features/data-processing/data-processing.component';
import {FileUploadComponent} from './features/file-upload/file-upload.component';
import {StudentListComponent} from './features/student-management/student-list/student-list.component';
import {StudentDetailComponent} from './features/student-management/student-detail/student-detail.component';
import {EditStudentComponent} from './features/student-management/edit-student/edit-student.component';
import {StudentReportComponent} from './features/student-report/student-report/student-report.component'; // Import AuthGuard

export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'data-generation', component: DataGenerationComponent, canActivate: [AuthGuard]},
  {path: 'data-processing', component: DataProcessingComponent, canActivate: [AuthGuard]},
  {path: 'file-upload',  component: FileUploadComponent, canActivate: [AuthGuard]},
  {path: 'student-management',  component: StudentListComponent, canActivate: [AuthGuard]},
  { path: 'student-details/:id', component: StudentDetailComponent, canActivate: [AuthGuard] },
  { path: 'edit-student/:id', component: EditStudentComponent },
  { path: 'student-report', component: StudentReportComponent },

  {path: '', redirectTo: '/login', pathMatch: 'full'}
];
