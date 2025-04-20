import { Component } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { DataGenerationService } from '../../core/services/data-generation.service';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import {MatProgressSpinner} from '@angular/material/progress-spinner';

@Component({
  selector: 'app-data-generation',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinner,
    MatProgressSpinner
  ],
  templateUrl: './data-generation.component.html',
  styleUrls: ['./data-generation.component.scss']
})
export class DataGenerationComponent {
  recordCount = new FormControl(100, [
    Validators.required,
    Validators.min(0),
    Validators.max(10000000)
  ]);
  isLoading = false;

  constructor(
    private dataService: DataGenerationService,
    private snackBar: MatSnackBar
  ) {}

  generateData() {
    if (this.recordCount.invalid) return;

    this.isLoading = true;
    const count = this.recordCount.value ?? 100;

    this.dataService.generateStudentData(count).subscribe({
      next: (response) => {
        this.snackBar.open(
          `Success! ${response.message} File: ${response.filePath}`,
          'Close',
          { duration: 5000 }
        );
      },
      error: (err) => {
        this.snackBar.open(
          `Error: ${err.error?.message || 'Failed to generate data'}`,
          'Close',
          { duration: 5000 }
        );
      },
      complete: () => {
        this.recordCount.reset();
        this.isLoading = false;
      }
    });
  }
}
