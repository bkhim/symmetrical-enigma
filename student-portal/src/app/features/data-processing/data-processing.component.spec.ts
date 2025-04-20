import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DataProcessingComponent } from './data-processing.component';
import { DataProcessingService } from '../../core/services/data-processing.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of, throwError } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import {By} from '@angular/platform-browser';

describe('DataProcessingComponent', () => {
  let component: DataProcessingComponent;
  let fixture: ComponentFixture<DataProcessingComponent>;
  let dataProcessingServiceMock: jasmine.SpyObj<DataProcessingService>;
  let snackBarMock: jasmine.SpyObj<MatSnackBar>;

  beforeEach(async () => {
    // Create mocks for services
    dataProcessingServiceMock = jasmine.createSpyObj('DataProcessingService', ['processExcelToCsv']);
    snackBarMock = jasmine.createSpyObj('MatSnackBar', ['open']);

    await TestBed.configureTestingModule({
      declarations: [DataProcessingComponent],
      imports: [
        CommonModule,
        MatCardModule,
        MatButtonModule,
        MatProgressSpinnerModule,
        MatSnackBarModule
      ],
      providers: [
        { provide: DataProcessingService, useValue: dataProcessingServiceMock },
        { provide: MatSnackBar, useValue: snackBarMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DataProcessingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // Trigger initial change detection
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should call processData() and show success message when the service returns a response', () => {
    // Mock successful response
    const mockResponse = { message: 'File processed successfully', csvPath: '/path/to/csv' };
    dataProcessingServiceMock.processExcelToCsv.and.returnValue(of(mockResponse));

    component.processData();

    // Ensure the service method was called
    expect(dataProcessingServiceMock.processExcelToCsv).toHaveBeenCalled();

    // Simulate the success response and check if snack bar is called with the correct message
    expect(snackBarMock.open).toHaveBeenCalledWith(
      `Success! ${mockResponse.message} CSV: ${mockResponse.csvPath}`,
      'Close',
      { duration: 5000 }
    );

    // Check if the spinner is turned off (loading false)
    expect(component.isLoading).toBeFalse();
  });

  it('should call processData() and show error message when the service throws an error', () => {
    // Mock error response
    const mockError = { error: { message: 'An error occurred' } };
    dataProcessingServiceMock.processExcelToCsv.and.returnValue(throwError(mockError));

    component.processData();

    // Ensure the service method was called
    expect(dataProcessingServiceMock.processExcelToCsv).toHaveBeenCalled();

    // Simulate the error response and check if snack bar is called with the correct error message
    expect(snackBarMock.open).toHaveBeenCalledWith(
      `Error: ${mockError.error?.message || 'Failed to process data'}`,
      'Close',
      { duration: 5000 }
    );

    // Check if the spinner is turned off (loading false)
    expect(component.isLoading).toBeFalse();
  });

  it('should set isLoading to true when processData is called', () => {
    // Mock successful response
    dataProcessingServiceMock.processExcelToCsv.and.returnValue(of({ message: 'success', csvPath: '/path' }));

    component.processData();
    expect(component.isLoading).toBeTrue();
  });

  it('should disable the button when loading', () => {
    const button = fixture.debugElement.query(By.css('button'));

    // Initial state
    expect(button.nativeElement.disabled).toBeFalsy();

    // Trigger loading
    component.isLoading = true;
    fixture.detectChanges();

    // After loading
    expect(button.nativeElement.disabled).toBeTruthy();
  });
});
