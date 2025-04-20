package com.studentportal.controller;

import com.studentportal.dto.LoginRequest;
import com.studentportal.dto.LoginResponse;
import com.studentportal.model.Student;
import com.studentportal.repository.StudentRepository;
import com.studentportal.security.JwtUtil;
import com.studentportal.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Handles authentication requests
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate the JWT token using the UserDetailsImpl (the principal)
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtil.generateJwtToken(userDetails.getUsername());

        Student student = studentRepository.findByStudentId(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        LoginResponse response = new LoginResponse(jwt, student.getFirstName(), student.getLastName());

        return ResponseEntity.ok(response);
    }
}
