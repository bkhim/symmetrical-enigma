package com.studentportal.service;

import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);  // Pagination applied here
    }

    public List<String> getClasses() {
        return studentRepository.findDistinctClasses();
    }

    // Fetch student by numeric ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id); // Using numeric ID to query the DB
    }

    // Fetch student by studentId (string identifier)
    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId); // Using studentId string
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Update student by numeric ID
    public Student updateStudent(Long id, Student studentDetails) {
        Optional<Student> studentOpt = getStudentById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            student.setClassName(studentDetails.getClassName());
            student.setScore(studentDetails.getScore());
            student.setStatus(studentDetails.getStatus());
            student.setDob(studentDetails.getDob());
            return studentRepository.save(student);
        }
        throw new RuntimeException("Student not found with id: " + id);  // Handle the case where the student does not exist
    }

    // Delete student by ID
    public void deleteStudent(Long studentId) {
        Optional<Student> studentOpt = getStudentById(studentId);
        if (studentOpt.isPresent()) {
            studentRepository.delete(studentOpt.get());
        } else {
            throw new RuntimeException("Student not found with id: " + studentId);  // Handle if student is not found
        }
    }

    // Method to get the count of active students
    public long getActiveStudentCount() {
        return studentRepository.count();  // Count all students, adjust if needed for active-only
    }
}
