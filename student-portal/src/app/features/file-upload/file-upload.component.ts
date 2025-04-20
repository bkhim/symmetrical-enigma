import { Component } from '@angular/core';
import { FileUploadService } from '../../core/services/file-upload.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { MatProgressSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-file-upload',
  imports: [
    MatIcon,
    MatButton,
    MatProgressSpinner,
    NgIf
  ],
  templateUrl: './file-upload.component.html'
})
export class FileUploadComponent {
  selectedFile: File | null = null;
  isUploading = false;

  constructor(
    private uploadService: FileUploadService,
    private snackBar: MatSnackBar
  ) {}

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedFile = input.files?.[0] || null;
  }

  uploadFile(): void {
    if (!this.selectedFile) return;

    this.isUploading = true;
    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.uploadService.uploadFile(formData).subscribe({
      next: (response: any) => {
        // Check if the response has a success message
        const successMessage = response?.message || 'Students imported successfully!';
        this.snackBar.open(successMessage, 'Close', { duration: 3000 });
        this.resetForm();
      },
      error: (err: any) => {
        const errorMessage = err?.error?.message || 'Server error';
        this.snackBar.open(`Upload failed: ${errorMessage}`, 'Close');
      },
      complete: () => this.isUploading = false
    });
  }

  private resetForm(): void {
    this.selectedFile = null;
    // Reset file input
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }
}
