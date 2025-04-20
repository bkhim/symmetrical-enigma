import { Component } from '@angular/core';
import { DataProcessingService } from '../../core/services/data-processing.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-data-processing',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule
  ],
  templateUrl: './data-processing.component.html',
  styleUrls: ['./data-processing.component.scss']
})
export class DataProcessingComponent {
  isLoading = false;

  constructor(
    private dataProcessingService: DataProcessingService,
    private snackBar: MatSnackBar
  ) {}

  processData() {
    this.isLoading = true;
    this.dataProcessingService.processExcelToCsv().subscribe({
      next: (response) => {
        this.snackBar.open(
          `Success! ${response.message} CSV: ${response.csvPath}`,
          'Close',
          { duration: 5000 }
        );
      },
      error: (err) => {
        this.snackBar.open(
          `Error: ${err.error?.message || 'Failed to process data'}`,
          'Close',
          { duration: 5000 }
        );
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}
