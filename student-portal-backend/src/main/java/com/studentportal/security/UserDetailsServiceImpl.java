package com.studentportal.security;

import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom user details service for loading student credentials
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final StudentRepository studentRepository;

    public UserDetailsServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found with ID: " + studentId));

        return UserDetailsImpl.build(student);
    }
}