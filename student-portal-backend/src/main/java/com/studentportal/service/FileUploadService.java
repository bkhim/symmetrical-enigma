package com.studentportal.service;

import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public FileUploadService(StudentRepository studentRepository,
                             PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public int processExcelFile(MultipartFile file) throws IOException {
        List<Student> students = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                // Skip header row
                if (row.getRowNum() == 0) continue;

                Student student = new Student();
                student.setStudentId(generateStudentId(row));
                student.setPassword(passwordEncoder.encode("defaultPassword")); // Temporary password
                student.setFirstName(getCellStringValue(row.getCell(1)));
                student.setLastName(getCellStringValue(row.getCell(2)));
                student.setDob(getCellDateValue(row.getCell(3)));
                student.setClassName(getCellStringValue(row.getCell(4)));
                student.setScore(getAdjustedScore(row.getCell(5)));
                student.setStatus(true); // Default active status
                student.setPhotoPath("");

                students.add(student);
            }
        }

        return studentRepository.saveAll(students).size();
    }

    private String generateStudentId(Row row) {
        Cell cell = row.getCell(0);
        if (cell == null) return "STD-" + UUID.randomUUID().toString().substring(0, 8);

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "STD-" + UUID.randomUUID().toString().substring(0, 8);
        };
    }

    private Integer getAdjustedScore(Cell cell) {
        if (cell == null) return 55; // Default minimum score

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) (cell.getNumericCellValue() + 5); // Add 5 as required
            case STRING:
                try {
                    // If the cell is a string, try parsing it as a number
                    return (int) (Double.parseDouble(cell.getStringCellValue()) + 5);
                } catch (NumberFormatException e) {
                    // Handle cases where the string cannot be parsed as a number
                    return 55; // Default value if parsing fails
                }
            default:
                return 55; // Default value for other cell types
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }


    private LocalDate getCellDateValue(Cell cell) {
        if (cell == null) return LocalDate.now();

        switch (cell.getCellType()) {
            case NUMERIC:
                // Check if it's a date (Excel stores dates as numeric values)
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate();
                }
                break;
            case STRING:
                // Try to parse the string as a date (if the date is in a string format)
                String cellValue = cell.getStringCellValue();
                try {
                    return LocalDate.parse(cellValue);  // Try parsing string to LocalDate
                } catch (Exception e) {
                    return LocalDate.now(); // Return current date if parsing fails
                }
            default:
                break;
        }
        return LocalDate.now();  // Return current date as fallback
    }

}