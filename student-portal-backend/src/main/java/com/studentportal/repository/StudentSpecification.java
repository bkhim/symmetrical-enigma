package com.studentportal.repository;

import com.studentportal.model.Student;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

/**
 * Dynamic query specifications for Student filtering
 * - Used in complex search operations
 * - Builds predicates at runtime
 */
public class StudentSpecification {

    public static Specification<Student> hasStatus(Boolean status) {
        return (root, query, cb) ->
                status != null ? cb.equal(root.get("status"), status) : null;
    }

    public static Specification<Student> hasClass(String className) {
        return (root, query, cb) ->
                className != null ? cb.equal(root.get("className"), className) : null;
    }

    public static Specification<Student> dobBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) ->
                (start != null && end != null) ?
                        cb.between(root.get("dob"), start, end) : null;
    }

    public static Specification<Student> searchByIdOrName(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) return null;
            return cb.or(
                    cb.like(root.get("studentId"), "%" + searchTerm + "%"),
                    cb.like(root.get("firstName"), "%" + searchTerm + "%"),
                    cb.like(root.get("lastName"), "%" + searchTerm + "%")
            );
        };
    }
}