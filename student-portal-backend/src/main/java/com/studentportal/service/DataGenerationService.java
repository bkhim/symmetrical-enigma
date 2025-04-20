package com.studentportal.service;

import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class DataGenerationService {

    private final StudentRepository studentRepository;
    private final String[] CLASS_NAMES = {"Class1", "Class2", "Class3", "Class4", "Class5"};
    private final Random random = new Random();


    public DataGenerationService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public String generateStudentData(int count) throws IOException {
        List<Student> existingStudents = studentRepository.findAll();

        if (existingStudents.isEmpty()) {
            throw new IllegalStateException("No students available in the database to duplicate.");
        }

        Workbook workbook = new XSSFWorkbook(); // Create .xlsx file
        Sheet sheet = workbook.createSheet("Students");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "First Name", "Last Name", "DOB", "Class", "Score", "Status"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Duplicate and write students
        int written = 0;
        int rowNum = 1;

        while (written < count) {
            for (Student student : existingStudents) {
                if (written >= count) break;

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue("user" + String.format("%06d", written + 1)); // New ID
                row.createCell(1).setCellValue(student.getFirstName());
                row.createCell(2).setCellValue(student.getLastName());
                row.createCell(3).setCellValue(student.getDob().toString());
                row.createCell(4).setCellValue(student.getClassName());
                row.createCell(5).setCellValue(student.getScore());
                row.createCell(6).setCellValue(student.getStatus() ? "Active" : "Inactive");

                written++;
            }
        }

        // Save to file
        String filePath = "C:/var/log/applications/API/dataprocessing/students_" +
                System.currentTimeMillis() + ".xlsx";
        Files.createDirectories(Paths.get(filePath).getParent());
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
        }
        workbook.close();

        return filePath;
    }


    private String generateRandomName(int minLen, int maxLen) {
        Random random = new Random();
        int length = minLen + random.nextInt(maxLen - minLen + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    private Date generateRandomDate() {
        Random random = new Random();
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2010, 12, 31);
        long randomDay = start.toEpochDay() + random.nextInt((int) (end.toEpochDay() - start.toEpochDay()));
        return Date.from(LocalDate.ofEpochDay(randomDay).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}