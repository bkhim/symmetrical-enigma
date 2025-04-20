package com.studentportal.service;

import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class DataProcessingService {

    private static final String processingDir = "C:/var/log/applications/API/dataprocessing/";
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataProcessingService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String generateUniqueStudentId(String baseStudentId) {
        // Generate a unique random string by appending a UUID to the base studentId
        return baseStudentId + "_" + UUID.randomUUID().toString();
    }

    @Transactional
    public String processLatestExcel() throws IOException {
        // 1. Find latest Excel file
        Path excelFile = findLatestExcelFile();

        // 2. Read and process data from the Excel file
        List<Student> students = readAndProcessExcel(excelFile);

        // 3. Save to database (insert or update)
        saveStudentsToDatabase(students);

        // 4. Write to CSV file and return the path
        return writeToCsv(students);
    }

    private Path findLatestExcelFile() throws IOException {
        return Files.list(Paths.get(processingDir))
                .filter(path -> path.toString().endsWith(".xlsx"))
                .max(Comparator.comparingLong(path -> path.toFile().lastModified()))
                .orElseThrow(() -> new FileNotFoundException("No Excel files found"));
    }

    private List<Student> readAndProcessExcel(Path excelFile) throws IOException {
        List<Student> students = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(excelFile.toFile())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Student student = new Student();

                try {
                    // Use student_id as String (unique identifier)
                    String studentId = getCellString(row.getCell(0));
                    // Modify the studentId to ensure it's unique
                    studentId = generateUniqueStudentId(studentId);

                    student.setStudentId(studentId); // student_id is used for the query

                    student.setFirstName(getCellString(row.getCell(1)));
                    student.setLastName(getCellString(row.getCell(2)));
                    student.setDob(LocalDate.parse(getCellString(row.getCell(3)))); // Format: yyyy-MM-dd
                    student.setClassName(getCellString(row.getCell(4)));

                    int score = Integer.parseInt(getCellString(row.getCell(5)));
                    student.setScore(score + 10);  // Adding 10 points to score

                    String status = getCellString(row.getCell(6));
                    student.setStatus("Active".equalsIgnoreCase(status));  // Set status to "Active" or "Inactive"

                    student.setPassword(this.passwordEncoder.encode("password123"));

                    students.add(student);
                } catch (Exception e) {
                    throw new RuntimeException("Error processing row " + row.getRowNum() + ": " + e.getMessage(), e);
                }
            }
        }

        return students;
    }

    private String getCellString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    yield String.valueOf((int) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula(); // Optional: evaluate if needed
            case BLANK -> "";
            default -> ""; // Handles ERROR or unknown types safely
        };
    }

    private String writeToCsv(List<Student> students) throws IOException {
        String csvPath = processingDir + "processed_students_" + System.currentTimeMillis() + ".csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath))) {
            // Write CSV header
            writer.println("StudentID,FirstName,LastName,DOB,Class,Score,Status");

            // Write student data to CSV
            for (Student student : students) {
                writer.println(
                        student.getStudentId() + "," +
                                student.getFirstName() + "," +
                                student.getLastName() + "," +
                                student.getDob() + "," +
                                student.getClassName() + "," +
                                student.getScore() + "," +
                                student.getStatus()
                );
            }
        }

        return csvPath;
    }

    private void saveStudentsToDatabase(List<Student> students) {
        for (Student student : students) {
            // Check if the student already exists by studentId
            Optional<Student> existingStudentOpt = studentRepository.findByStudentId(student.getStudentId());

            if (existingStudentOpt.isPresent()) {
                // If student exists, update the record
                Student existing = existingStudentOpt.get();
                existing.setFirstName(student.getFirstName());
                existing.setLastName(student.getLastName());
                existing.setDob(student.getDob());
                existing.setClassName(student.getClassName());
                existing.setScore(student.getScore());
                existing.setStatus(student.getStatus());

                studentRepository.save(existing); // Save the updated student
            } else {
                // If student doesn't exist, save as a new student
                studentRepository.save(student); // Save new student if not found
            }
        }
    }
}
