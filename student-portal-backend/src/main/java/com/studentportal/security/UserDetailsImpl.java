package com.studentportal.security;

import com.studentportal.model.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of UserDetails for student authentication
 */
public class UserDetailsImpl implements UserDetails {
    private final String studentId;
    private final String password;

    public UserDetailsImpl(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
    }

    public static UserDetailsImpl build(Student student) {
        return new UserDetailsImpl(
                student.getStudentId(),
                student.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // All students have the same basic role
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return studentId; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}