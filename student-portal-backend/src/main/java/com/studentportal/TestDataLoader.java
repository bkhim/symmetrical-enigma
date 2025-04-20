package com.studentportal;

import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Component
public class TestDataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    private final String[] CLASS_NAMES = {"Class1", "Class2", "Class3", "Class4", "Class5"};

    public TestDataLoader(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Comment this out if you don't want to clear the DB
        // studentRepository.deleteAll();

        long existing = studentRepository.count();
        if (existing >= 1_000_000) {
            System.out.println("Test data already exists. Skipping load.");
            return;
        }

        int total = 1_000_010;
        int batchSize = 1000;

        for (int batch = 0; batch < total / batchSize; batch++) {
            List<Student> students = new ArrayList<>();

            for (int i = 0; i < batchSize; i++) {
                int studentNum = batch * batchSize + i + 1;
                String studentId = "user" + String.format("%06d", studentNum);

                Student student = new Student();
                student.setStudentId(studentId);
                student.setPassword(passwordEncoder.encode("password123"));
                student.setFirstName(generateRandomName(5, 10));
                student.setLastName(generateRandomName(5, 10));
                student.setClassName(CLASS_NAMES[random.nextInt(CLASS_NAMES.length)]);
                student.setScore(55 + random.nextInt(31));
                student.setStatus(random.nextBoolean());

                int year = 2000 + random.nextInt(11);
                int month = 1 + random.nextInt(12);
                int day = generateRandomDay(year, month);
                student.setDob(LocalDate.of(year, month, day));

                // Optional: Set photo path to "default.jpg"
                student.setPhotoPath("default.jpg");

                students.add(student);
            }

            studentRepository.saveAll(students);
            System.out.println("Saved batch " + (batch + 1) + "/" + (total / batchSize));
        }

        System.out.println("✅ Finished creating 1,000,000 students.");
    }


    // Random name generation with first letter capitalized
    private String generateRandomName(int minLen, int maxLen) {
        int length = minLen + random.nextInt(maxLen - minLen + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        String name = sb.toString();
        return name.substring(0, 1).toUpperCase() + name.substring(1); // Capitalize the first letter
    }

    // Random day generation considering month length and leap years
    private int generateRandomDay(int year, int month) {
        int day = 1 + random.nextInt(28); // Default safe day
        if (month == 2) {
            if (isLeapYear(year)) {
                day = 1 + random.nextInt(29); // February in a leap year (29 days)
            } else {
                day = 1 + random.nextInt(28); // February in a non-leap year (28 days)
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            day = 1 + random.nextInt(30); // April, June, September, November (30 days)
        } else {
            day = 1 + random.nextInt(31); // All other months (31 days)
        }
        return day;
    }

    // Check if the given year is a leap year
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }
}
